package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.constant.OrderConstant;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderAddressVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderTargetAddressVO;
import com.sftc.web.model.vo.swaggerOrderRequest.SourceAddressVO;
import com.sftc.web.model.vo.swaggerOrderRequest.TargetAddressVO;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.TargetCoordinateVO;
import com.sftc.web.service.impl.MessageServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;
import static com.sftc.tools.constant.WXConstant.WX_template_id_1;


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
    private AddressBookMapper addressBookMapper;
    @Resource
    private MessageServiceImpl messageService;
    @Resource
    private OrderDao orderDao;

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

        if (requestObject.containsKey("request")) { // 同城
            return normalSameOrderCommit(requestBody);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("reason", "Param missing request");
            return APIUtil.submitErrorResponse("request参数缺失", map);
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
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("reason", "Param missing request");
            return APIUtil.submitErrorResponse("request参数缺失", map);
        }
    }

    //////////////////// Private Method ////////////////////

    /// 订单提交接口验参
    private String orderCommitVerify(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
        //同城单请求体为request对象
        boolean requestObject = jsonObject.containsKey("request");  // 同城

        if (!jsonObject.containsKey("order")) {
            return "参数order不能为空";
        }

        if (!requestObject)
            return "参数request不能为空";

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
        String ship_name = "";
        for (OrderExpressDTO oeDto : orderDTO.getOrderExpressList()) {
            if (oeDto.getShip_name()!=null){
                OrderExpress oe = gson.fromJson(gson.toJson(oeDto), OrderExpress.class);
                ship_name += oe.getShip_name() + ",";
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

                if (!(responseObject.containsKey("error") || responseObject.containsKey("errors"))) {
                    uuid = (String) responseObject.getJSONObject("request").get("uuid");
                    request_num = responseObject.getJSONObject("request").getString("request_num");
                    String pay_state = "WAIT_PAY";
                    boolean payed = responseObject.getJSONObject("request").getBoolean("payed");
                    if (payed) pay_state = "ALREADY_PAY";
                    else pay_state = "WAIT_PAY";

                    /// 数据库操作
                    String order_time = Long.toString(System.currentTimeMillis());
                    // 快递表更新  uuid / 预约时间 / 下单时间 / 订单号 / 快递状态
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time);
                    orderExpressMapper.updateOrderTime(uuid, order_time);
                    orderExpressMapper.updateOrderNumber(oe.getId(), request_num);
                    orderExpressMapper.updateOrderExpressStatus(oe.getId(), responseObject.getJSONObject("request").getString("status"), pay_state);

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
            messageArr[1] = "您的顺丰订单下单成功！收件人是：" + ship_name;
            String form_id = requestObject.getJSONObject("order").getString("form_id");
            messageService.sendWXTemplateMessage(user_id, messageArr, path, form_id, WX_template_id_1);
        }

        orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, orderDTO);
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
        String pay_state = null;
        if (!(respObject.containsKey("error") || respObject.containsKey("errors"))) {
            // 插入订单表
            orderDao.save(order);
            if (respObject.containsKey("request")) {
                JSONObject req = respObject.getJSONObject("request");
                if (req != null && req.containsKey("attributes")) {
                    JSONObject attrObj = req.getJSONObject("attributes");
                    attrStr = attrObj.toString();
                }
                if (req != null && req.containsKey("payed")) {
                    boolean payed = req.getBoolean("payed");
                    if (payed) {
                        pay_state = "ALREADY_PAY";
                    } else {
                        pay_state = "WAIT_PAY";
                    }
                }
            }

            String package_type = requestOBJ.getJSONArray("packages").getJSONObject(0).getString("package_type");
            String ship_name = targetAddressOBJ.getString("receiver");
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
                    package_type,
                    pay_state
            );

            if (reserve_time != null && !reserve_time.equals("")) {
                orderExpress.setRoute_state("PAYING");
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
            respObject.put("pay_state", pay_state);//支付状态

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
                messageArr[1] = "您的顺丰订单下单成功！收件人是：" + ship_name;
                String form_id = reqObject.getJSONObject("order").getString("form_id");
                messageService.sendWXTemplateMessage(reqObject.getJSONObject("order").getInt("sender_user_id"),
                        messageArr, path, form_id, WX_template_id_1);
            }

        } else {
            //手动操作事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return APIUtil.submitErrorResponse("提交失败", respObject);
        }

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /// 插入地址簿  要去重  通用地址簿插入utils
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

    ///普通同城下单使用的 一键添加2个地址簿 1个历史地址
    private void setupAddress2(Order order, OrderExpress oe) {
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


