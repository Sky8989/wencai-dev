package com.sftc.web.service.impl.logic.app;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_CREATEORDER_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;
import static com.sftc.tools.constant.WXConstant.WX_template_id_1;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.constant.OrderConstant;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressHistoryMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.model.entity.AddressHistory;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderVO.OrderAddressVO;
import com.sftc.web.model.vo.swaggerOrderVO.OrderTargetAddressVO;
import com.sftc.web.model.vo.swaggerOrderVO.SourceAddressVO;
import com.sftc.web.model.vo.swaggerOrderVO.TargetAddressVO;
import com.sftc.web.model.vo.swaggerRequestVO.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequestVO.TargetCoordinateVO;
import com.sftc.web.service.impl.MessageServiceImpl;

import net.sf.json.JSONObject;


@Component
public class OrderCommitLogic {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private AddressBookDao addressBookDao;
    @Resource
    private AddressHistoryMapper addressHistoryMapper;
    @Resource
    private AddressBookMapper addressBookMapper;
    @Resource
    private MessageServiceImpl messageService;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderExpressDao orderExpressDao;
    private Gson gson = new Gson();
    //////////////////// Public Method ////////////////////

    /**
     * 普通订单提交
     */
    public APIResponse normalOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verif 订单提交的接口验参
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);

        // 增加对emoji的过滤
        if (requestObject.containsKey("request")) { // 同城
            boolean containsEmoji = EmojiFilter.containsEmoji(requestObject.getJSONObject("request").getString("packages"));
            if (containsEmoji) {
                return APIUtil.paramErrorResponse("Don't input emoji");
            }
        }

        //通过请求的对象来判断同城还是大网，改版之后是怎么实现的？也是每个订单这样去判断吗？
        if (requestObject.containsKey("request")) { // 同城
            return normalSameOrderCommit(requestBody);
        } else { // 大网
            return normalNationOrderCommit(requestBody);
        }
    }

    /**
     * 好友订单提交
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)  //使用最高级别的事物防止提交过程中有好友包裹被填写
    public APIResponse friendOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);

        // 增加对emoji的过滤
        if (requestObject.containsKey("request")) { // 同城
            boolean containsEmoji = EmojiFilter.containsEmoji(requestObject.getJSONObject("request").getString("packages"));
            if (containsEmoji) {
                return APIUtil.paramErrorResponse("Don't input emoji");
            }
        }

        if (requestObject.containsKey("request")) { // 同城
            return friendSameOrderCommit(requestObject);
        } else { // 大网
            return friendNationOrderCommit(requestObject);
        }
    }

    /**
     * 大网预约订单提交
     */
    public APIResponse nationOrderReserveCommit(String order_id, long currentTimeMillis) {

        try {
            OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);
            if (orderDTO == null)
                return APIUtil.selectErrorResponse("订单不存在", null);
            //后期订单和快递改为一对一之后，请求为object对象，遍历里面的order对象来提交？
            for (OrderExpressDTO orderExpressDTO : orderDTO.getOrderExpressList()) {
                OrderExpress oe = gson.fromJson(gson.toJson(orderExpressDTO), OrderExpress.class);

                // 改为提前半小时下单，并且不下预约时间为一小时以前的单，这是为了排除服务器更新上线把定时器关了导致的时间间隔误差
                if (oe.getReserve_time().equals("") || oe.getReserve_time() == null) {
                    return APIUtil.submitErrorResponse("非预约单", null);
                }
                final long reserve_time = Long.parseLong(oe.getReserve_time());
                if (reserve_time < currentTimeMillis - 3600000L || reserve_time >= currentTimeMillis + 1800000L) {
                    return APIUtil.submitErrorResponse(oe.getId() + "--暂未到达预约时间", null);
                }

                // 大网订单提交参数
                JSONObject sf = new JSONObject();
                sf.put("orderid", oe.getUuid());
                sf.put("j_contact", orderDTO.getSender_name());
                sf.put("j_mobile", orderDTO.getSender_mobile());
                sf.put("j_tel", orderDTO.getSender_mobile());
                sf.put("j_country", "中国");
                sf.put("j_province", orderDTO.getSender_province());
                sf.put("j_city", orderDTO.getSender_city());
                sf.put("j_county", orderDTO.getSender_area());
                sf.put("j_address", orderDTO.getSender_addr());
                sf.put("d_contact", oe.getShip_name());
                sf.put("d_mobile", oe.getShip_mobile());
                sf.put("d_tel", oe.getShip_mobile());
                sf.put("d_country", "中国");
                sf.put("d_province", oe.getShip_province());
                sf.put("d_city", oe.getShip_city());
                sf.put("d_county", oe.getShip_area());
                sf.put("d_address", oe.getShip_addr());
                sf.put("pay_method", orderDTO.getPay_method());
                sf.put("express_type", orderDTO.getDistribution_method());
                // handle pay_method
                String pay_method = (String) sf.get("pay_method");
                if (pay_method != null && !pay_method.equals("")) {
                    if (pay_method.equals("FREIGHT_PREPAID")) { // FREIGHT_PREPAID 寄付 1
                        pay_method = "1";
                    } else if (pay_method.equals("FREIGHT_COLLECT")) { // FREIGHT_COLLECT 到付 2
                        pay_method = "2";
                    }
                    sf.remove("pay_method");
                    sf.put("pay_method", pay_method);
                }

                // 订单状态为其它的，不需要再次下单
                if (!oe.getState().equals("WAIT_HAND_OVER")) continue;
                // 订单编号不为空，说明已下单
                if (oe.getOrder_number() != null && !oe.getOrder_number().equals("")) continue;

                // 订单提交
                String paramStr = gson.toJson(JSONObject.fromObject(sf));
                HttpPost post = new HttpPost(SF_CREATEORDER_URL);
                post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
                String resultStr = APIPostUtil.post(paramStr, post);
                JSONObject resultObject = JSONObject.fromObject(resultStr);
                String messageType = (String) resultObject.get("Message_Type");

                if (resultObject.get("Message") != null || (messageType != null && messageType.contains("ERROR"))) {
                    logger.error("大网预约单提交失败: " + resultObject);
                } else {
                    // 存储快递信息
                    Order order1 = orderDao.findOne(orderDTO.getId());
                    order1.setRegion_type("REGION_NATION");
                    orderDao.save(order1);
                    String ordernum = resultObject.getString("ordernum");
                    oe.setOrder_number(ordernum);
                    oe.setState("WAIT_HAND_OVER");
                    orderExpressDao.save(oe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return APIUtil.getResponse(SUCCESS, null);
    }

    //////////////////// Private Method ////////////////////

    /// 订单提交接口验参
    private String orderCommitVerify(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
        //同城单请求体为request对象，大网单请求体为sf对象
        boolean requestObject = jsonObject.containsKey("request");  // 同城
        boolean sfObject = jsonObject.containsKey("sf");            // 大网

        if (!jsonObject.containsKey("order")) {
            return "参数order不能为空";
        }

        if (!requestObject && !sfObject) {
            return "参数sf和request不能都为空";
        } else if (requestObject && sfObject) {
            return "参数sf和request不能同时存在";
        }

        return null;
    }

    /// 好友同城订单提交
    @Transactional
    private APIResponse friendSameOrderCommit(JSONObject requestObject) {
    	Integer user_id = TokenUtils.getInstance().getUserId();
  		String request_num = ""; // 订单号
  		String uuid = ""; // 请求顺丰下单接口 返回的uuid
        // Param
        if (!requestObject.containsKey("order"))
            return APIUtil.paramErrorResponse("订单信息不完整");
        String order_id = requestObject.getJSONObject("order").getString("order_id");
        if (order_id == null || order_id.equals(""))
            return APIUtil.paramErrorResponse("order_id不能为空");
        if (!requestObject.containsKey("request"))
            return APIUtil.paramErrorResponse("订单信息不完整");
        JSONObject reqObject = requestObject.getJSONObject("request");
        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        reqObject.put("request_source", "C_WX_APP");
        reqObject.put("type", "NORMAL");    //默认为普通

        String reserve_time = "";
        if (requestObject.getJSONObject("order").containsKey("reserve_time")) {
            reserve_time = requestObject.getJSONObject("order").getString("reserve_time");
            if (reserve_time != null && !reserve_time.equals("")) {
                reqObject.remove("type");   //预约单type
                reqObject.put("type", "RESERVED");
            }
        }

        JSONObject attributeOBJ = reqObject.getJSONObject("attributes");
        if (attributeOBJ.containsKey("source"))
            attributeOBJ.remove("source");

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);
        String sendName = orderDTO.getSender_name(); //提交订单的用户名
        if (orderDTO == null) return APIUtil.submitErrorResponse("订单不存在", null);

        //增加对包裹数量的验证，确保是只有一个订单里只有一个同城包裹
        //后期更新订单与包裹一对一，但是好友件同城可以多包裹，同城订单走这个逻辑？
        if (orderDTO.getOrderExpressList().size() != 1)
            return APIUtil.submitErrorResponse("Order infomation has been changed, please check again!", null);

        for (OrderExpressDTO oeDto : orderDTO.getOrderExpressList()) {
            OrderExpress oe = gson.fromJson(gson.toJson(oeDto), OrderExpress.class);
            // 拼接同城订单参数中的 source 和 target
            SourceAddressVO source = new SourceAddressVO();
            OrderAddressVO address = new OrderAddressVO();
            address.setProvince(orderDTO.getSender_province());
            address.setCity(orderDTO.getSender_city());
            address.setRegion(orderDTO.getSender_area());
            address.setStreet(orderDTO.getSender_addr());
            address.setReceiver(orderDTO.getSender_name());
            address.setMobile(orderDTO.getSender_mobile());
            CoordinateVO coordinate = new CoordinateVO();
            coordinate.setLongitude(orderDTO.getLongitude());
            coordinate.setLatitude(orderDTO.getLatitude());
            source.setAddress(address);
            source.setCoordinate(coordinate);

            TargetAddressVO target = new TargetAddressVO();
            OrderTargetAddressVO targetAddress = new OrderTargetAddressVO();
            targetAddress.setProvince(oe.getShip_province());
            targetAddress.setCity(oe.getShip_city());
            targetAddress.setRegion(oe.getShip_area());
            targetAddress.setStreet(oe.getShip_addr());
            targetAddress.setReceiver(oe.getShip_name());
            targetAddress.setMobile(oe.getShip_mobile());
            TargetCoordinateVO targetCoordinate = new TargetCoordinateVO();
            targetCoordinate.setLongitude(oe.getLongitude());
            targetCoordinate.setLatitude(oe.getLatitude());
            target.setAddress(targetAddress);
            target.setCoordinate(targetCoordinate);

            requestObject.getJSONObject("request").put("source", source);
            requestObject.getJSONObject("request").put("target", target);
            if (reserve_time != null && !reserve_time.equals("")) {
                String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                requestObject.getJSONObject("request").put("reserve_time", reserveTime);
            }

            // TODO 把门牌号加到下单的参数json中
            Object removeStreet = requestObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").remove("street");
            String newStreet = removeStreet.toString() + orderDTO.getSupplementary_info();
            requestObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").put("street", newStreet);
            Object removeStreet2 = requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").remove("street");
            String newStreet2 = removeStreet2.toString() + oe.getSupplementary_info();
            requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").put("street", newStreet2);

            /// Request
            Object tempObj = JSONObject.toBean(requestObject);
            // tempJsonObj 是为了保证对顺丰接口的请求体的完整，不能包含其它的键值对，例如接口的请求参数"order"
            JSONObject tempJsonObj = JSONObject.fromObject(tempObj);
            tempJsonObj.remove("order");

            // Param
            String paramStr = gson.toJson(JSONObject.fromObject(tempJsonObj));
            // POST
            HttpPost post = new HttpPost(SF_REQUEST_URL);
//            String token = (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token");
            TokenUtils instance = TokenUtils.getInstance();
            String token = instance.getAccess_token();
            String userUUID = instance.getUserUUID();
            requestObject.getJSONObject("request").getJSONObject("merchant").put("access_token", token);
            requestObject.getJSONObject("request").getJSONObject("merchant").put("uuid", userUUID);

            post.addHeader("PushEnvelope-Device-Token", token);
            String resultStr = APIPostUtil.post(paramStr, post);
            JSONObject responseObject = JSONObject.fromObject(resultStr);

//            if (!responseObject.containsKey("error")) {
            if (!(responseObject.containsKey("error") || responseObject.containsKey("errors"))) {
                 uuid = (String) responseObject.getJSONObject("request").get("uuid");
                 request_num = responseObject.getJSONObject("request").getString("request_num");

//                /// 数据库操作
//                // 订单表更新订单区域类型
//                Order order1 = orderDao.findOne(order_id);
//                order1.setRegion_type("REGION_SAME");
//                orderDao.save(order1);
//                // 快递表更新uuid和预约时间
//                oe.setUuid(uuid);
//                oe.setReserve_time(reserve_time);
////                orderExpressDao.save(oe);
//                String order_time = Long.toString(System.currentTimeMillis());
//                oe.setOrder_time(order_time);
//                oe.setOrder_number(request_num);
//                oe.setState(responseObject.getJSONObject("request").getString("status"));
//                //更新订单状态
//                orderExpressDao.save(oe);

                /// 数据库操作
                // 订单表更新订单区域类型
                orderMapper.updateOrderRegionType(order_id, "REGION_SAME");
                String order_time = Long.toString(System.currentTimeMillis());
                // 快递表更新  uuid / 预约时间 / 下单时间 / 订单号 / 快递状态
                orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time);
                orderExpressMapper.updateOrderTime(uuid, order_time);
                orderExpressMapper.updateOrderNumber(oe.getId(), request_num);
                orderExpressMapper.updateOrderExpressStatus(oe.getId(), responseObject.getJSONObject("request").getString("status"));

                // 插入地址
                //setupAddress(order, oe);
                //使用新的地址插入工具
                //setupAddress2(order, oe);

            } else { // error
                String message;
                try {
                    if (responseObject.containsKey("error")) {
                        message = responseObject.getJSONObject("error").getString("message");
                    } else {
                        message = responseObject.getJSONArray("errors").getJSONObject(0).getString("message");
                    }
                    //手动操作事务回滚
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                } catch (Exception e) {
                    message = "下单失败";
                }
                return APIUtil.submitErrorResponse(message, responseObject);
            }
        }

        String path = ""; // 好友同城微信模板跳转链接
  		// 发送微信模板消息
  		if (requestObject.getJSONObject("order").containsKey("form_id")
  				&& StringUtils.isNotEmpty(requestObject.getJSONObject("order").getString("form_id"))) {
  			String[] messageArr = new String[2];
  			if (StringUtils.isNotEmpty(order_id) && StringUtils.isNotEmpty(uuid)) {
  				path = OrderConstant.MYSTERY_REGION_SAME_LINK + "?uuid=" + uuid + "&order_id=" + order_id;
  			}
  			this.logger.info("----好友同城微信模板跳转链接--" + path);
  			messageArr[0] = request_num;
  			messageArr[1] = "您的顺丰订单下单成功(好友同城)！寄件人是：" + sendName;
  			String form_id = requestObject.getJSONObject("order").getString("form_id");
  			messageService.sendWXTemplateMessage(user_id, messageArr, path, form_id, WX_template_id_1);
  		}
        
        orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, orderDTO);
    }

    /// 好友大网订单提交
    private synchronized APIResponse friendNationOrderCommit(JSONObject requestObject) {
        // handle param
        String order_id = requestObject.getJSONObject("order").getString("order_id");
        JSONObject orderOBJ = requestObject.getJSONObject("order");
        if (order_id == null || order_id.equals(""))
            return APIUtil.paramErrorResponse("order_id不能为空");
        String distribution = requestObject.getJSONObject("sf").getString("express_type");
        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
//        Order order1 = orderDao.findOne(order_id);
//        order1.setRegion_type("REGION_NATION");
//        order1.setDistribution_method(distribution);
//        orderDao.save(order1);
        //事务问题,先存在查的改为统一使用Mybatis
        String region_type = "REGION_NATION";
        orderMapper.updateRegionAndDistributionById(order_id, region_type, distribution);

        OrderDTO orderDto = orderMapper.selectOrderDetailByOrderIdForUpdate(order_id);
        if (orderDto == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        for (OrderExpressDTO oe : orderDto.getOrderExpressList()) {
            // 拼接大网订单地址参数
            JSONObject sf = requestObject.getJSONObject("sf");
            sf.put("orderid", oe.getUuid());
            sf.put("j_contact", orderDto.getSender_name());
            sf.put("j_mobile", orderDto.getSender_mobile());
            sf.put("j_tel", orderDto.getSender_mobile());
            sf.put("j_country", "中国");
            sf.put("j_province", orderDto.getSender_province());
            sf.put("j_city", orderDto.getSender_city());
            sf.put("j_county", orderDto.getSender_area());
            sf.put("j_address", orderDto.getSender_addr());
            sf.put("d_contact", oe.getShip_name());
            sf.put("d_mobile", oe.getShip_mobile());
            sf.put("d_tel", oe.getShip_mobile());
            sf.put("d_country", "中国");
            sf.put("d_province", oe.getShip_province());
            sf.put("d_city", oe.getShip_city());
            sf.put("d_county", oe.getShip_area());
            sf.put("d_address", oe.getShip_addr());

            // handle pay_method
            String pay_method = (String) sf.get("pay_method");
            if (pay_method != null && !pay_method.equals("")) {
                if (pay_method.equals("FREIGHT_PREPAID")) { // FREIGHT_PREPAID 寄付 1
                    pay_method = "1";
                } else if (pay_method.equals("FREIGHT_COLLECT")) { // FREIGHT_COLLECT 到付 2
                    pay_method = "2";
                }
                sf.remove("pay_method");
                sf.put("pay_method", pay_method);
            }

            if (!oe.getState().equals("WAIT_FILL")) {
                if (reserve_time != null && !reserve_time.equals("")) { // 预约件处理
                    //事务问题,先存在查的改为统一使用Mybatis
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), oe.getUuid(), reserve_time);
                } else {
                    // 处理street 和 门牌号的拼接
                    Object j_address = sf.remove("j_address");
                    Object d_address = sf.remove("d_address");
                    sf.put("j_address", j_address + orderDto.getSupplementary_info());
                    sf.put("d_address", d_address + oe.getSupplementary_info());

                    // 立即提交订单
                    String paramStr = gson.toJson(JSONObject.fromObject(sf));
                    HttpPost post = new HttpPost(SF_CREATEORDER_URL);
                    post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
                    String resultStr = APIPostUtil.post(paramStr, post);
                    JSONObject jsonObject = JSONObject.fromObject(resultStr);

                    // 增加对下单结果的判断  含有error Message 或者 没有ordernum 都算是提交失败
                    if (jsonObject.containsKey("error") || jsonObject.containsKey("Message") || !jsonObject.containsKey("ordernum")) {
                        //手动操作事务回滚
                        // TODO: 回滚了哪个事务？为什么要回滚？为什么会抛异常？不回滚会怎么样？
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        // TODO: 直接手动把`region_type`置空，有没有其他问题？
                        Order order2 = orderDao.findOne(order_id);
                        order2.setRegion_type(null);
                        orderDao.save(order2);
                        String message = "下单失败";
                        if (jsonObject.containsKey("Message")) {    //特殊错误信息的处理
                            message = jsonObject.getString("Message");
                            if (message != null) {
                                message = message.replace(" ", "");
                                if (message.equals("人工确认")) {
                                    message = "该地区不在服务范围内";
                                } else if (message.equals("校验码错误") || message.equals("")) {
                                    message = "请勿输入特殊字符";
                                }
                            }
                        } else if (jsonObject.containsKey("error")) {
                            message = jsonObject.getJSONObject("error").getString("message");
                            message = message.replace(" ", "");
                            if (message != null) {
                                if (message.equals("人工确认")) {
                                    message = "该地区不在服务范围内";
                                } else if (message.equals("校验码错误") || message.equals("")) {
                                    message = "请勿输入特殊字符";
                                }
                            }
                        }
                        return APIUtil.submitErrorResponse(message, jsonObject);
                    } else {
                        // 存储订单信息
                        //事务问题,先存在查的改为统一使用Mybatis
                        String order_time = Long.toString(System.currentTimeMillis());
                        String ordernum = jsonObject.getString("ordernum");

                        orderExpressMapper.updateOrderTime(oe.getUuid(), order_time);
                        orderExpressMapper.updateOrderNumber(oe.getId(), ordernum);
                        orderExpressMapper.updateOrderExpressStatus(oe.getId(), "WAIT_HAND_OVER");


                        // 插入地址
                        //setupAddress(order, oe);
                        //使用新的地址插入工具
                        //setupAddress2(order, oe);

                    }
                }
            }
        }
        orderDto = orderMapper.selectOrderDetailByOrderId(order_id);
        return APIUtil.getResponse(SUCCESS, orderDto);
    }

    /// 普通同城订单提交
    public APIResponse normalSameOrderCommit(Object object) {

        JSONObject reqObject = JSONObject.fromObject(object);
        JSONObject requestOBJ = reqObject.getJSONObject("request");
        JSONObject orderOBJ = reqObject.getJSONObject("order");
        JSONObject sourceOBJ = requestOBJ.getJSONObject("source");
        JSONObject targetOBJ = requestOBJ.getJSONObject("target");
        JSONObject sourceAddressOBJ = sourceOBJ.getJSONObject("address");
        JSONObject targetAddressOBJ = targetOBJ.getJSONObject("address");

        //处理非必传参数package里的comments
        String comments = requestOBJ.getJSONArray("packages").getJSONObject(0).containsKey("comments") ? requestOBJ.getJSONArray("packages").getJSONObject(0).getString("comments") : "";

        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        requestOBJ.put("request_source", "C_WX_APP");
        requestOBJ.put("type", "NORMAL");    //默认为普通

        //处理supplementary_info非必填项的问题
        if (!sourceAddressOBJ.containsKey("supplementary_info")) {
            sourceAddressOBJ.put("supplementary_info", "");
        }
        if (!targetAddressOBJ.containsKey("supplementary_info")) {
            targetAddressOBJ.put("supplementary_info", "");
        }

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                SFOrderHelper.getOrderId(),
                (String) requestOBJ.get("pay_type"),
                (String) requestOBJ.get("product_type"),
                (String) sourceAddressOBJ.get("receiver"),
                (String) sourceAddressOBJ.get("mobile"),
                (String) sourceAddressOBJ.get("province"),
                (String) sourceAddressOBJ.get("city"),
                (String) sourceAddressOBJ.get("region"),
                (String) sourceAddressOBJ.get("street"),
                (String) sourceAddressOBJ.get("supplementary_info"), //增加门牌号
                sourceOBJ.getJSONObject("coordinate").getDouble("longitude"),
                sourceOBJ.getJSONObject("coordinate").getDouble("latitude"),
                "ORDER_BASIS",
                Integer.parseInt((String) orderOBJ.get("sender_user_id"))
        );
        order.setImage((String) orderOBJ.get("image"));
        order.setVoice((String) orderOBJ.get("voice"));
        order.setWord_message((String) orderOBJ.get("word_message"));
        if (!orderOBJ.getString("gift_card_id").equals("")) {
            order.setGift_card_id(Integer.parseInt((String) orderOBJ.get("gift_card_id")));
        } else {
            order.setGift_card_id(0);
        }
        order.setVoice_time(Integer.parseInt((String) orderOBJ.get("voice_time")));
        order.setRegion_type("REGION_SAME");

        HttpPost post = new HttpPost(SF_REQUEST_URL);
        TokenUtils instance = TokenUtils.getInstance();
        String token = instance.getAccess_token();
        String userUUID = instance.getUserUUID();
        requestOBJ.getJSONObject("merchant").put("access_token", token);
        requestOBJ.getJSONObject("merchant").put("uuid", userUUID);
//        post.addHeader("PushEnvelope-Device-Token", (String) requestOBJ.getJSONObject("merchant").get("access_token"));
        post.addHeader("PushEnvelope-Device-Token", token);

        // 预约时间处理
        String reserve_time = orderOBJ.containsKey("reserve_time") ? orderOBJ.getString("reserve_time") : null;
        if (reserve_time != null && !reserve_time.equals("")) {
            String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestOBJ.put("reserve_time", reserveTime);
            // 预约单type
            requestOBJ.put("type", "RESERVED");
        }
        // 面对面下单
        int is_directed = 0;
        JSONObject attriubuteOBJ = requestOBJ.getJSONObject("attributes");
        if (attriubuteOBJ.containsKey("source")) {
            if (attriubuteOBJ.getString("source").equals("DIRECTED")) {
                // 面对面单type
                requestOBJ.put("type", "DIRECTED");
                is_directed = 1;
            }
            //移除 attributes 中的 source
            attriubuteOBJ.remove("source");
        }

        JSONObject tempObject = JSONObject.fromObject(reqObject);
        tempObject.remove("order");
        //  把门牌号加到下单的参数json中
        String oldSourceAddressStreet = sourceAddressOBJ.getString("street");
        String oldTargetAddressStreet = targetAddressOBJ.getString("street");

        //  把门牌号加到下单的参数json中
        Object removeStreet = tempObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").remove("street");
        String newStreet = removeStreet.toString() + order.getSupplementary_info();
        tempObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").put("street", newStreet);
        Object removeStreet2 = tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").remove("street");
        String newStreet2 = removeStreet2.toString() + tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("supplementary_info");
        tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").put("street", newStreet2);

        // 向sf下单
        String requestSFParamStr = gson.toJson(tempObject);
        JSONObject respObject = JSONObject.fromObject(APIPostUtil.post(requestSFParamStr, post));
        String uuId = "";

        if (respObject.containsKey("error")) {
        	
            return APIUtil.submitErrorResponse(respObject.getJSONObject("error").getString("message"), respObject.getJSONObject("error"));
        }

        // 获取面对面取件码（到付）
        String directed_code = null;
        if (respObject.containsKey("request")) {
            JSONObject req = respObject.getJSONObject("request");
            if (req != null && req.containsKey("attributes")) {
            	uuId = respObject.getJSONObject("request").getString("uuid");
                JSONObject attributuOBJ = req.getJSONObject("attributes");
                if (attributuOBJ.containsKey("directed_code")) {
                    directed_code = attributuOBJ.getString("directed_code");
                    is_directed = 1;
                    logger.info("面对面取件码:" + directed_code);
                }
            }
        }

        String attrStr = null;
        if (!(respObject.containsKey("error") || respObject.containsKey("errors"))) {
            // 插入订单表
            orderDao.save(order);
            if (respObject.containsKey("request")) {
                JSONObject req = respObject.getJSONObject("request");
                if (req != null && req.containsKey("attributes")) {
                    JSONObject attrObj = req.getJSONObject("attributes");
                    attrStr = attrObj.toString();
                }
            }

            String package_type = requestOBJ.getJSONArray("packages").getJSONObject(0).getString("package_type");
            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    Long.toString(System.currentTimeMillis()),
                    respObject.getJSONObject("request").getString("request_num"),
                    targetAddressOBJ.getString("receiver"),
                    targetAddressOBJ.getString("mobile"),
                    targetAddressOBJ.getString("province"),
                    targetAddressOBJ.getString("city"),
                    targetAddressOBJ.getString("region"),
                    oldTargetAddressStreet,
                    targetAddressOBJ.getString("supplementary_info"),
                    requestOBJ.getJSONArray("packages").getJSONObject(0).getString("weight"),
                    requestOBJ.getJSONArray("packages").getJSONObject(0).getString("type"),
                    comments, //增加快递包裹描述comments
                    "WAIT_HAND_OVER",
                    Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")),
                    order.getId(),
                    respObject.getJSONObject("request").getString("uuid"),
                    targetOBJ.getJSONObject("coordinate").getDouble("latitude"),
                    targetOBJ.getJSONObject("coordinate").getDouble("longitude"),
                    directed_code,
                    attrStr,
                    is_directed,
                    package_type
            );

            if (reserve_time != null && !reserve_time.equals("")) {
                orderExpress.setState("PAYING");
            }
            orderExpress.setReserve_time(reserve_time);
//            orderExpressDao.save(orderExpress);
            orderExpressMapper.addOrderExpress2(orderExpress);
            // 插入地址
            //setupAddress(order, orderExpress);
            /**
             * 使用地址映射插入工具
             */
            String create_time = Long.toString(System.currentTimeMillis());
            setupAddress2(order, orderExpress);

            // 返回结果添加订单编号
            respObject.put("order_id", order.getId());
            // 添加是否面对面下单
            respObject.put("is_directed", is_directed);
            respObject.put("package_type", package_type);

			String path = ""; // 普通同城跳转链接
            // 发送微信模板消息
            if (reqObject.getJSONObject("order").containsKey("form_id")
                    && StringUtils.isNotEmpty(reqObject.getJSONObject("order").getString("form_id"))) {
            	if (StringUtils.isNotEmpty(order.getId())) {
					path = OrderConstant.BASIS_REGION_SAME_LINK + "?order_id=" + order.getId() + "&uuid=" + uuId;
				}
				this.logger.info("--------普通同城跳转链接 ----" + path);
				
                String[] messageArr = new String[2];
                messageArr[0] = respObject.getJSONObject("request").getString("request_num");
                messageArr[1] = "您的顺丰订单下单成功(同城)！寄件人是："
                        + (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver");
                String form_id = reqObject.getJSONObject("order").getString("form_id");
                messageService.sendWXTemplateMessage( reqObject.getJSONObject("order").getInt("sender_user_id") ,
                        messageArr, path, form_id, WX_template_id_1);
            }

        } else {
            //手动操作事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return APIUtil.submitErrorResponse("提交失败", respObject);
        }

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /// 普通大网订单提交
    public APIResponse normalNationOrderCommit(Object object) {

        JSONObject requestObject = JSONObject.fromObject(object);
        JSONObject orderObject = requestObject.getJSONObject("order");
        JSONObject sf = requestObject.getJSONObject("sf");
        JSONObject packagesOBJ = requestObject.getJSONArray("packages").getJSONObject(0);
        String orderId = SFOrderHelper.getOrderNumber();
        String distribution = requestObject.getJSONObject("sf").getString("express_type");
        // handle pay_method
        String pay_method = (String) sf.get("pay_method");
        if (pay_method != null && !pay_method.equals("")) {
            if (pay_method.equals("FREIGHT_PREPAID")) { // FREIGHT_PREPAID 寄付 1
                pay_method = "1";
            } else if (pay_method.equals("FREIGHT_COLLECT")) { // FREIGHT_COLLECT 到付 2
                pay_method = "2";
            }
            sf.remove("pay_method");
            sf.put("pay_method", pay_method);
        }
        sf.put("orderid", orderId);

        //处理supplementary_info非必填项的问题
        if (!sf.containsKey("j_supplementary_info")) {
            sf.put("j_supplementary_info", "");
        }
        if (!sf.containsKey("d_supplementary_info")) {
            sf.put("d_supplementary_info", "");
        }

        double j_longitude = orderObject.containsKey("j_longitude") ? orderObject.getDouble("j_longitude") : 0;
        double j_latitude = orderObject.containsKey("j_latitude") ? orderObject.getDouble("j_latitude") : 0;

        // 插入订单表
        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                SFOrderHelper.getOrderId(),
                (String) sf.get("pay_method"),
                distribution,
                (String) sf.get("j_contact"),
                (String) sf.get("j_tel"),
                (String) sf.get("j_province"),
                (String) sf.get("j_city"),
                (String) sf.get("j_county"),
                (String) sf.get("j_address"),
                sf.getString("j_supplementary_info"),//增加门牌号
                j_longitude,
                j_latitude,
                "ORDER_BASIS",
                Integer.parseInt((String) orderObject.get("sender_user_id"))
        );
        order.setImage((String) orderObject.get("image"));
        order.setVoice((String) orderObject.get("voice"));
        order.setWord_message((String) orderObject.get("word_message"));
        if (!orderObject.getString("gift_card_id").equals("")) {
            order.setGift_card_id(Integer.parseInt((String) orderObject.get("gift_card_id")));
        } else {
            order.setGift_card_id(0);
        }
        order.setVoice_time(Integer.parseInt((String) orderObject.get("voice_time")));
        order.setRegion_type("REGION_NATION");
//        orderDao.save(order);
        orderMapper.addOrder2(order);

        double d_longitude = 0;
        double d_latitude = 0;
        if (orderObject.containsKey("d_longitude")) {
            d_longitude = orderObject.getDouble("d_longitude");
        }
        if (orderObject.containsKey("d_latitude")) {
            d_latitude = orderObject.getDouble("d_latitude");
        }

        // 插入快递表
        OrderExpress orderExpress = new OrderExpress(
                Long.toString(System.currentTimeMillis()),
                Long.toString(System.currentTimeMillis()),
                "",
                (String) sf.get("d_contact"),
                (String) sf.get("d_tel"),
                (String) sf.get("d_province"),
                (String) sf.get("d_city"),
                (String) sf.get("d_county"),
                (String) sf.get("d_address"),
                sf.getString("d_supplementary_info"),//增加门牌号
                packagesOBJ.getString("weight"),
                packagesOBJ.getString("type"),
                packagesOBJ.containsKey("comments") ? packagesOBJ.getString("comments") : "",//增加快递包裹描述comments
                "WAIT_HAND_OVER",
                Integer.parseInt((String) orderObject.get("sender_user_id")),
                order.getId(),
                orderId,
                d_latitude,
                d_longitude,
                packagesOBJ.getString("package_type")
        );
        orderExpress.setReserve_time((String) requestObject.getJSONObject("order").get("reserve_time"));
//        orderExpressDao.save(orderExpress);
        orderExpressMapper.addOrderExpress(orderExpress);

        // 插入地址
//        setupAddress(order, orderExpress);
        nationInsertAddressBookAndAddressHistory(order.getSender_user_id(), orderObject, sf, Long.toString(System.currentTimeMillis()));

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        JSONObject responseObject = new JSONObject();
        if (reserve_time == null || reserve_time.equals("")) {
            // 处理 street和门牌号的拼接
            JSONObject paramTemp = JSONObject.fromObject(sf);
            Object j_address = paramTemp.remove("j_address");
            Object d_address = paramTemp.remove("d_address");
            paramTemp.put("j_address", j_address + paramTemp.getString("j_supplementary_info"));
            paramTemp.put("d_address", d_address + paramTemp.getString("d_supplementary_info"));

            String str = gson.toJson(paramTemp);

            // POST
            HttpPost post = new HttpPost(SF_CREATEORDER_URL);
            post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
            String res = APIPostUtil.post(str, post);
            responseObject = JSONObject.fromObject(res);
            // 增加对下单结果的判断  含有error Message 或者 没有ordernum 都算是提交失败
            if (responseObject.containsKey("error") || responseObject.containsKey("Message") || !responseObject.containsKey("ordernum")) {
                String message = "下单失败";
                if (responseObject.containsKey("Message")) {  //人工确认错误信息处理
                    message = responseObject.getString("Message");
                    message = message.replace(" ", "");
                    if (message != null) {
                        if (message.equals("人工确认")) {
                            message = "该地区不在服务范围内";
                        } else if (message.equals("校验码错误") || message.equals("")) {
                            message = "请勿输入特殊字符";
                        }
                    }
                } else if (responseObject.containsKey("error")) {
                    message = responseObject.getJSONObject("error").getString("message");
                    message = message.replace(" ", "");
                    //人工确认错误信息处理
                    if (message != null) {
                        if (message.equals("人工确认")) {
                            message = "该地区不在服务范围内";
                        } else if (message.equals("校验码错误") || message.equals("")) {
                            message = "请勿输入特殊字符";
                        }
                    }
                }
                try {
                    // 手动操作事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                } catch (Exception e) {
                    return APIUtil.logicErrorResponse(e.getLocalizedMessage(), e);
                }
                return APIUtil.submitErrorResponse(message, responseObject);
            } else {
                // 返回结果添加订单编号
                String ordernum = responseObject.getString("ordernum");
//                orderExpress.setOrder_number(ordernum);
//                orderExpressDao.save(orderExpress);
                orderExpressMapper.updateOrderNumber(orderExpress.getId(), ordernum);
            }
        } else { // 预约件
            responseObject.put("message", "大网订单预约成功");
        }

        /// 发送微信模板消息
//        if (orderObject.containsKey("form_id") && !"".equals(orderObject.getString("form_id"))) {
//            String[] messageArr = new String[2];
//            messageArr[0] = orderExpress.getOrder_number();
//            messageArr[1] = "您的顺丰订单下单成功(大网)！寄件人是：" + sf.get("j_contact");
//            String form_id = orderObject.getString("form_id");
//            messageService.sendWXTemplateMessage(Integer.parseInt((String) orderObject.get("sender_user_id")),
//                    messageArr, "", form_id, WX_template_id_1);
//        }

        Order orderData = orderMapper.selectOrderDetailByOrderId(order.getId());
        responseObject.put("order", orderData);

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /// 插入地址
    private void setupAddress(Order order, OrderExpress oe) {
        // 插入地址表
        com.sftc.web.model.entity.Address address = new com.sftc.web.model.entity.Address(oe);
        addressMapper.addAddress(address);

        // 插入历史地址表
        List<com.sftc.web.model.entity.Address> addressList = addressMapper.selectAddressByPhoneAndLongitudeAndLatitude(oe.getShip_mobile(), oe.getLongitude(), oe.getLatitude());
        if (addressList.size() == 1) { // 插入的时候就根据手机号和经纬度去重，相同的手机号和经纬度 只会存在一个历史地址记录
            AddressHistory addressHistory = new AddressHistory();
            addressHistory.setUser_id(order.getSender_user_id());
            addressHistory.setAddress_id(address.getId());
            addressHistory.setCreate_time(Long.toString(System.currentTimeMillis()));
            addressHistory.setIs_delete(0);
            int is_mystery = order.getOrder_type().equals("ORDER_MYSTERY") ? 1 : 0;
            addressHistory.setIs_mystery(is_mystery);
            addressHistoryMapper.insertAddressHistory(addressHistory);
        }
    }

    /// 插入地址簿  要去重
    // 通用地址簿插入utils
    public void insertAddressBookUtils(
            String address_type, String address_book_type, int user_id_sender, int user_id_ship, String name, String phone,
            String province, String city, String area, String address, String supplementary_info,
            String create_time, double longitude, double latitude) {

        com.sftc.web.model.entity.Address addressParam = new com.sftc.web.model.entity.Address(
                user_id_ship, name, phone,
                province, city, area, address, supplementary_info,
                longitude, latitude, create_time
        );

        // 查找重复信息
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressForRemoveDuplicate(user_id_sender,
                address_type, address_book_type, name, phone,
                province, city, area, address, supplementary_info);

        if (addressBookDTOList.size() == 0) {// 0代表无重复信息
            //执行插入操作
            addressMapper.addAddress(addressParam);
            AddressBook addressBook = new AddressBook(user_id_sender, addressParam.getId(), 0, 0, address_type, address_book_type, create_time);
            addressBookDao.save(addressBook);
        }
        // addressBookList的size如果大于0 代表已经有相同地址
        // 不做插入处理
    }

    /// 普通同城下单使用的 一键添加2个地址簿 1个历史地址
    private void tempInsertAddressBookAndAddressHistory(
            String create_time, JSONObject orderOBJ
            , JSONObject sourceAddressOBJ, JSONObject sourceOBJ
            , JSONObject targetAddressOBJ, JSONObject targetOBJ) {
        // 插入地址簿 寄件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "sender",
//                orderOBJ.getInt("sender_user_id"),
//                orderOBJ.getInt("sender_user_id"),
//                sourceAddressOBJ.getString("receiver"),
//                sourceAddressOBJ.getString("mobile"),
//                sourceAddressOBJ.getString("province"),
//                sourceAddressOBJ.getString("city"),
//                sourceAddressOBJ.getString("region"),
//                sourceAddressOBJ.getString("street"),
//                sourceAddressOBJ.getString("supplementary_info"),
//                create_time,
//                (Double) sourceOBJ.getJSONObject("coordinate").get("longitude"),
//                (Double) sourceOBJ.getJSONObject("coordinate").get("latitude")
//        );
        // 插入地址簿 收件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "ship",
//                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
//                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
//                targetAddressOBJ.getString("receiver"),
//                targetAddressOBJ.getString("mobile"),
//                targetAddressOBJ.getString("province"),
//                targetAddressOBJ.getString("city"),
//                targetAddressOBJ.getString("region"),
//                targetAddressOBJ.getString("street"),
//                targetAddressOBJ.getString("supplementary_info"),
//                create_time,
//                (Double) targetOBJ.getJSONObject("coordinate").get("longitude"),
//                (Double) targetOBJ.getJSONObject("coordinate").get("latitude")
//        );
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
                0,// 这里的地址是属于寄件人的
                targetAddressOBJ.getString("receiver"),
                targetAddressOBJ.getString("mobile"),
                targetAddressOBJ.getString("province"),
                targetAddressOBJ.getString("city"),
                targetAddressOBJ.getString("region"),
                targetAddressOBJ.getString("street"),
                targetAddressOBJ.getString("supplementary_info"),
                create_time,
                targetOBJ.getJSONObject("coordinate").getDouble("longitude"),
                targetOBJ.getJSONObject("coordinate").getDouble("latitude")
        );
    }

    /// 普通大网下单使用的 一键添加2个地址簿 1个历史地址
    private void nationInsertAddressBookAndAddressHistory(int user_id_sender, JSONObject orderObject, JSONObject sf, String create_time) {
        // 插入地址簿 寄件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "sender",
//                user_id_sender,
//                user_id_sender,
//                sf.getString("j_contact"),
//                sf.getString("j_mobile"),
//                sf.getString("j_province"),
//                sf.getString("j_city"),
//                sf.getString("j_county"),
//                sf.getString("j_address"),
//                sf.getString("j_supplementary_info"),
//                create_time,
//                orderObject.getDouble("j_longitude"),
//                orderObject.getDouble("j_latitude")
//        );
        // 插入地址簿 收件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "ship",
//                user_id_sender,// 这里的地址是属于寄件人的
//                user_id_sender,// 这里的地址是属于寄件人的
//                sf.getString("d_contact"),
//                sf.getString("d_mobile"),
//                sf.getString("d_province"),
//                sf.getString("d_city"),
//                sf.getString("d_county"),
//                sf.getString("d_address"),
//                sf.getString("d_supplementary_info"),
//                create_time,
//                orderObject.getDouble("d_longitude"),
//                orderObject.getDouble("d_latitude")
//        );
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                user_id_sender,
                0,// 历史地址都是自己写的
                sf.getString("d_contact"),
                sf.getString("d_mobile"),
                sf.getString("d_province"),
                sf.getString("d_city"),
                sf.getString("d_county"),
                sf.getString("d_address"),
                sf.getString("d_supplementary_info"),
                create_time,
                orderObject.getDouble("d_longitude"),
                orderObject.getDouble("d_latitude")
        );
    }

    ///普通同城下单使用的 一键添加2个地址簿 1个历史地址
    private void setupAddress2(Order order, OrderExpress oe) {
        // 插入地址簿 寄件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "sender",
//                order.getSender_user_id(),
//                order.getSender_user_id(),// 地址是收件人的
//                order.getSender_name(),
//                order.getSender_mobile(),
//                order.getSender_province(),
//                order.getSender_city(),
//                order.getSender_area(),
//                order.getSender_addr(),
//                order.getSupplementary_info(),
//                order.getCreate_time(), order.getLongitude(),
//                order.getLatitude()
//        );
        // 插入地址簿 收件人,不勾选保存地址簿，不用插入
//        insertAddressBookUtils("address_book", "ship",
//                order.getSender_user_id(),
//                oe.getShip_user_id(),//地址是寄件人的
//                oe.getShip_name(),
//                oe.getShip_mobile(),
//                oe.getShip_province(),
//                oe.getShip_city(),
//                oe.getShip_area(),
//                oe.getShip_addr(),
//                oe.getSupplementary_info(),
//                oe.getCreate_time(),
//                oe.getLongitude(),
//                oe.getLatitude());
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                oe.getSender_user_id(), //这个地址是属于某个用户的地址 但是地址内容是历史地址 保存的是寄件时产生的收件人地址
                oe.getShip_user_id(),
                oe.getShip_name(),
                oe.getShip_mobile(),
                oe.getShip_province(),
                oe.getShip_city(),
                oe.getShip_area(),
                oe.getShip_addr(),
                oe.getSupplementary_info(),
                oe.getCreate_time(), oe.getLongitude(),
                oe.getLatitude()
        );
    }
}

