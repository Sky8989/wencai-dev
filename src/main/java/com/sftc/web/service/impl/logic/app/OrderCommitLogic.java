package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.constant.OrderConstant;
import com.sftc.tools.sf.SfOrderHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.tools.utils.InsertAddressBookUtil;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.*;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.TargetCoordinateVO;
import com.sftc.web.service.impl.MessageServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.SF_REQUEST_URL;
import static com.sftc.tools.constant.WxConstant.WX_TEMPLATE_ID1;


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
    @Resource
    private OrderConstant orderConstant;

    private Gson gson = new Gson();
    //////////////////// Public Method ////////////////////

    /**
     * 普通订单提交
     */
    public ApiResponse normalOrderCommit(OrderRequestVO orderRequestVO) {

        // 增加对emoji的过滤
        boolean containsEmoji = EmojiFilter.containsEmoji(orderRequestVO.getRequest().getPackages().toString());
        if (containsEmoji) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Don't input emoji");
        }
        return normalSameOrderCommit(orderRequestVO);
    }

    /**
     * 好友订单提交
     * 使用最高级别的事物防止提交过程中有好友包裹被填写
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ApiResponse friendOrderCommit(FriendOrderRequestVO friendOrderRequestVO) {

        // 增加对emoji的过滤
        boolean containsEmoji = EmojiFilter.containsEmoji(friendOrderRequestVO.getRequest().getPackages().toString());
        if (containsEmoji) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Don't input emoji");
        }
        return friendSameOrderCommit(friendOrderRequestVO);
    }

    /**
     * 好友同城订单提交
     */
    @Transactional(rollbackFor = Exception.class)
    private ApiResponse friendSameOrderCommit(FriendOrderRequestVO friendOrderRequestVO) {
        String userUuid = TokenUtils.getInstance().getUserUUID();
        // 订单号
        String requestNum = "";
        // 请求顺丰下单接口 返回的uuid
        String uuid = "";

        JSONObject reqObject = JSONObject.fromObject(friendOrderRequestVO.getRequest());
        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        reqObject.put("request_source", "C_WX_APP");
        //默认为普通
        reqObject.put("type", "NORMAL");

        String reserveTime = "";
        if (StringUtils.isNotBlank(friendOrderRequestVO.getOrder().getReserve_time())) {
            reserveTime = friendOrderRequestVO.getOrder().getReserve_time();
            //预约单type
            reqObject.remove("type");
            reqObject.put("type", "RESERVED");
        }

        if (StringUtils.isNotBlank(friendOrderRequestVO.getRequest().getAttributes().getSource())) {
            friendOrderRequestVO.getRequest().getAttributes().setSource(null);
        }

        String orderId = friendOrderRequestVO.getOrder().getOrder_id();
        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);

        if (orderDTO == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        //增加对包裹数量的验证，确保是只有一个订单里只有一个同城包裹
        //后期更新订单与包裹一对一，但是好友件同城可以多包裹，同城订单走这个逻辑？
        String shipName = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderExpressDTO oeDto : orderDTO.getOrderExpressList()) {
            if (oeDto.getShip_name() != null) {
                OrderExpress oe = gson.fromJson(gson.toJson(oeDto), OrderExpress.class);
                stringBuilder.append(shipName);
                stringBuilder.append(oe.getShip_name());
                stringBuilder.append(",");
                shipName = stringBuilder.toString();
                if (StringUtils.isNotBlank(oe.getOrder_number())) {
                    return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "请勿重复提交订单");
                }
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

                reqObject.put("source", source);
                reqObject.put("target", target);
                if (StringUtils.isNotBlank(reserveTime)) {
                    reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    friendOrderRequestVO.getOrder().setReserve_time(reserveTime);
                } else {
                    friendOrderRequestVO.getOrder().setReserve_time(null);
                }

                //TODO 把门牌号加到下单的参数json中
                Object removeStreet = reqObject.getJSONObject("source").getJSONObject("address").remove("street");
                String newStreet = removeStreet.toString() + orderDTO.getSupplementary_info();
                reqObject.getJSONObject("source").getJSONObject("address").put("street", newStreet);
                Object removeStreet2 = reqObject.getJSONObject("target").getJSONObject("address").remove("street");
                String newStreet2 = removeStreet2.toString() + oe.getSupplementary_info();
                reqObject.getJSONObject("target").getJSONObject("address").put("street", newStreet2);

                TokenUtils instance = TokenUtils.getInstance();
                String token = instance.getAccessToken();
                String userUUID = instance.getUserUUID();
                JSONObject merchantOBJ = new JSONObject();

                merchantOBJ.put("uuid", userUUID);
                reqObject.put("merchant", merchantOBJ);

                // Param
                JSONObject requestObj = new JSONObject();
                requestObj.put("request", reqObject);
                String paramStr = gson.toJson(JSONObject.fromObject(requestObj));
                // POST
                HttpPost post = new HttpPost(SF_REQUEST_URL);

                post.addHeader("PushEnvelope-Device-Token", token);
                String resultStr = ApiPostUtil.post(paramStr, post);
                JSONObject responseObject = JSONObject.fromObject(resultStr);

                if (!(responseObject.containsKey(CustomConstant.ERROR) || responseObject.containsKey(CustomConstant.ERRORS))) {
                    uuid = (String) responseObject.getJSONObject(CustomConstant.SAME_REQUEST).get("uuid");
                    requestNum = responseObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("request_num");
                    String payState;
                    boolean payed = responseObject.getJSONObject(CustomConstant.SAME_REQUEST).getBoolean(CustomConstant.PAYED);
                    payState = payed ? "ALREADY_PAY" : "WAIT_PAY";

                    // 数据库操作
                    String orderTime = Long.toString(System.currentTimeMillis());
                    String quoteUuid = reqObject.getJSONObject("quote").getString("uuid");
                    // 快递表更新  uuid / 预约时间 / 下单时间 / 订单号 / 快递状态
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserveTime);
                    orderExpressMapper.updateOrderTime(uuid, orderTime, quoteUuid);
                    orderExpressMapper.updateOrderNumber(oe.getId(), requestNum);
                    orderExpressMapper.updateOrderExpressStatus(oe.getId(), responseObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("status"), payState);

                } else { // error
                    String message;
                    try {
                        if (responseObject.containsKey(CustomConstant.ERROR)) {
                            message = responseObject.getJSONObject(CustomConstant.ERROR).getString("message");
                        } else {
                            message = responseObject.getJSONArray(CustomConstant.ERRORS).getJSONObject(0).getString("message");
                        }
                    } catch (Exception e) {
                        message = "下单失败";
                    }
                    return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), message, responseObject);
                }
            }

        }

       /*
        * 用来判断跳转的链接 是
        * OrderConstant.mystery_region_same_link  		false
        * 或 OrderConstant.mamy_mystery_region_same_link true
        */
        boolean flag = orderDTO.orderExpressList.size() != 1;
        // 好友同城微信模板跳转链接
        String path = "";
        // 发送微信模板消息
        if (StringUtils.isNotBlank(friendOrderRequestVO.getOrder().getForm_id())) {
            String[] messageArr = new String[2];
            if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(uuid)) {
                if (!flag) {
                    path = orderConstant.MYSTERY_REGION_SAME_LINK + "?order_id=" + orderId + "&uuid=" + uuid;
                } else {
                    path = orderConstant.MAMY_MYSTERY_REGION_SAME_LINK + "?order_id=" + orderId;
                }
            }
            this.logger.info("----好友同城微信模板跳转链接--" + path);
            messageArr[0] = requestNum;
            messageArr[1] = "您的顺丰订单下单成功！收件人是：" + shipName;
            String formId = friendOrderRequestVO.getOrder().getForm_id();
            messageService.sendWXTemplateMessage(userUuid, messageArr, path, formId, WX_TEMPLATE_ID1);
        }

        orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);

        return ApiUtil.getResponse(SUCCESS, orderDTO);
    }

    /**
     * 普通同城订单提交
     */
    private ApiResponse normalSameOrderCommit(OrderRequestVO orderRequestVO) {
        String userUuid = TokenUtils.getInstance().getUserUUID();

        RequestVO requestVO = orderRequestVO.getRequest();
        OrderMessageVO orderMessageVO = orderRequestVO.getOrder();
        SourceAddressVO sourceAddressVO = orderRequestVO.getRequest().getSource();
        TargetAddressVO targetAddressVO = orderRequestVO.getRequest().getTarget();
        OrderAddressVO orderAddressVO = sourceAddressVO.getAddress();
        OrderTargetAddressVO orderTargetAddressVO = targetAddressVO.getAddress();

        //处理非必传参数package里的comments
        String comments = StringUtils.isNotBlank(requestVO.getPackages().get(0).getComments()) ? requestVO.getPackages().get(0).getComments() : "";

        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        requestVO.setRequest_source("C_WX_APP");
        //默认为普通
        requestVO.setType("NORMAL");

        //处理supplementary_info非必填项的问题
        if (StringUtils.isBlank(orderAddressVO.getSupplementary_info())) {
            orderAddressVO.setSupplementary_info("");
        }
        if (StringUtils.isBlank(orderTargetAddressVO.getSupplementary_info())) {
            orderTargetAddressVO.setSupplementary_info("");
        }

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                SfOrderHelper.getOrderId(),
                orderRequestVO.getRequest().getPay_type(),
                orderRequestVO.getRequest().getProduct_type(),
                orderAddressVO.getReceiver(),
                orderAddressVO.getMobile(),
                orderAddressVO.getProvince(),
                orderAddressVO.getCity(),
                orderAddressVO.getRegion(),
                orderAddressVO.getStreet(),
                //增加门牌号
                orderAddressVO.getSupplementary_info(),
                sourceAddressVO.getCoordinate().getLongitude(),
                sourceAddressVO.getCoordinate().getLatitude(),
                "ORDER_BASIS",
                userUuid
        );
        order.setImage(orderMessageVO.getImage());
        order.setVoice(orderMessageVO.getVoice());
        order.setWord_message((orderMessageVO.getWord_message()));
        if (StringUtils.isNotBlank(orderMessageVO.getGift_card_id())) {
            order.setGift_card_id(Integer.parseInt(orderMessageVO.getGift_card_id()));
        } else {
            order.setGift_card_id(0);
        }
        order.setVoice_time(Integer.parseInt(orderMessageVO.getVoice_time()));

        HttpPost post = new HttpPost(SF_REQUEST_URL);
        TokenUtils instance = TokenUtils.getInstance();
        String token = instance.getAccessToken();
        String userUUID = instance.getUserUUID();
        OrderMerchantVO orderMerchantVO = new OrderMerchantVO();

        orderMerchantVO.setUuid(userUUID);
        requestVO.setMerchant(orderMerchantVO);

        post.addHeader("PushEnvelope-Device-Token", token);

        // 面对面下单
        int isDirected = 0;
        AttributesVO attributesVO = orderRequestVO.getRequest().getAttributes();

        if (attributesVO.getSource() != null && attributesVO.getSource().equals(CustomConstant.DIRECTED)) {
            // 面对面单type
            requestVO.setType(CustomConstant.DIRECTED);
            isDirected = 1;
        }

        JSONObject tempObject = JSONObject.fromObject(orderRequestVO);
        // 预约时间处理
        String reserveTime = StringUtils.isNotBlank(orderMessageVO.getReserve_time()) ? orderMessageVO.getReserve_time() : null;
        if (StringUtils.isNotBlank(reserveTime)) {
            reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            tempObject.getJSONObject(CustomConstant.SAME_REQUEST).put("reserve_time", reserveTime);
            // 预约单type
            tempObject.getJSONObject(CustomConstant.SAME_REQUEST).put("type", "RESERVED");
        }
        tempObject.remove("order");
        //  把门牌号加到下单的参数json中
        String oldTargetAddressStreet = orderTargetAddressVO.getStreet();

        //  把门牌号加到下单的参数json中
        Object removeStreet = tempObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("source").getJSONObject("address").remove("street");
        String newStreet = removeStreet.toString() + order.getSupplementary_info();
        tempObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("source").getJSONObject("address").put("street", newStreet);
        Object removeStreet2 = tempObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("target").getJSONObject("address").remove("street");
        String newStreet2 = removeStreet2.toString() + tempObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("target").getJSONObject("address").getString("supplementary_info");
        tempObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("target").getJSONObject("address").put("street", newStreet2);

        // 向sf下单
        String requestSFParamStr = gson.toJson(tempObject);
        JSONObject respObject = JSONObject.fromObject(ApiPostUtil.post(requestSFParamStr, post));
        String uuId = "";

        if (respObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), respObject.getJSONObject(CustomConstant.ERROR).getString("message"),
                    respObject.getJSONObject(CustomConstant.ERROR));
        }

        // 获取面对面取件码（到付）
        String directedCode = null;
        String directedCodeStr = "directed_code";
        if (respObject.containsKey(CustomConstant.SAME_REQUEST)) {
            JSONObject req = respObject.getJSONObject(CustomConstant.SAME_REQUEST);
            if (req != null && req.containsKey(CustomConstant.ATTRIBUTES)) {
                uuId = respObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("uuid");
                JSONObject attributeOBJ = req.getJSONObject(CustomConstant.ATTRIBUTES);
                if (attributeOBJ.containsKey(directedCodeStr)) {
                    directedCode = attributeOBJ.getString("directed_code");
                    isDirected = 1;
                    logger.info("面对面取件码:" + directedCode);
                }
            }
        }

        String attrStr = null;
        String payState = null;
        if (!(respObject.containsKey(CustomConstant.ERROR) || respObject.containsKey(CustomConstant.ERRORS))) {
            // 插入订单表
            orderDao.save(order);
            if (respObject.containsKey(CustomConstant.SAME_REQUEST)) {
                JSONObject req = respObject.getJSONObject(CustomConstant.SAME_REQUEST);
                if (req != null && req.containsKey(CustomConstant.ATTRIBUTES)) {
                    JSONObject attrObj = req.getJSONObject(CustomConstant.ATTRIBUTES);
                    attrStr = attrObj.toString();
                }
                if (req != null && req.containsKey(CustomConstant.PAYED)) {
                    boolean payed = req.getBoolean(CustomConstant.PAYED);
                    payState = payed ? "ALREADY_PAY" : "WAIT_PAY";
                }
            }

            String packageType = requestVO.getPackages().get(0).getPackage_type();
            String shipName = orderTargetAddressVO.getReceiver();
            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    Long.toString(System.currentTimeMillis()),
                    respObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("request_num"),
                    shipName,
                    orderTargetAddressVO.getMobile(),
                    orderTargetAddressVO.getProvince(),
                    orderTargetAddressVO.getCity(),
                    orderTargetAddressVO.getRegion(),
                    oldTargetAddressStreet,
                    orderTargetAddressVO.getSupplementary_info(),
                    orderRequestVO.getRequest().getPackages().get(0).getWeight(),
                    orderRequestVO.getRequest().getPackages().get(0).getType(),
                    //增加快递包裹描述comments
                    comments,
                    "WAIT_HAND_OVER",
                    userUuid,
                    order.getId(),
                    respObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("uuid"),
                    targetAddressVO.getCoordinate().getLatitude(),
                    targetAddressVO.getCoordinate().getLongitude(),
                    directedCode,
                    attrStr,
                    isDirected,
                    packageType,
                    payState,
                    respObject.getJSONObject(CustomConstant.SAME_REQUEST).getJSONObject("quote").getString("uuid")
            );

            if (StringUtils.isNotBlank(reserveTime)) {
                orderExpress.setRoute_state("PAYING");
            }
            orderExpress.setReserve_time(reserveTime);
            orderExpressMapper.addOrderExpress2(orderExpress);

            // 使用地址映射插入工具
            setupAddress2(orderExpress);

            // 返回结果添加订单编号
            respObject.put("order_id", order.getId());
            // 添加是否面对面下单
            respObject.put("is_directed", isDirected);
            respObject.put("package_type", packageType);
            //支付状态
            respObject.put("pay_state", payState);

            // 普通同城跳转链接
            String path = "";
            // 发送微信模板消息
            if (StringUtils.isNotBlank(orderMessageVO.getForm_id())) {
                if (StringUtils.isNotEmpty(order.getId())) {
                    path = orderConstant.BASIS_REGION_SAME_LINK + "?order_id=" + order.getId() + "&uuid=" + uuId;
                }
                this.logger.info("--------普通同城跳转链接 ----" + path);

                String[] messageArr = new String[2];
                messageArr[0] = respObject.getJSONObject(CustomConstant.SAME_REQUEST).getString("request_num");
                messageArr[1] = "您的顺丰订单下单成功！收件人是：" + shipName;
                String formId = orderMessageVO.getForm_id();
                messageService.sendWXTemplateMessage(userUUID, messageArr, path, formId, WX_TEMPLATE_ID1);
            }

        } else {
            //手动操作事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "提交失败", respObject);
        }

        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 普通同城下单使用的 一键添加2个地址簿 1个历史地址
     */

    private void setupAddress2(OrderExpress oe) {
        // 插入历史地址
        new InsertAddressBookUtil().insertAddressBookUtils("address_history", "ship",
                //这个地址是属于某个用户的地址 但是地址内容是历史地址 保存的是寄件时产生的收件人地址
                oe.getSender_user_uuid(),
                oe.getShip_user_uuid(),
                oe.getShip_name(),
                oe.getShip_mobile(),
                oe.getShip_province(),
                oe.getShip_city(),
                oe.getShip_area(),
                oe.getShip_addr(),
                oe.getSupplementary_info(),
                oe.getCreate_time(), oe.getLongitude(),
                oe.getLatitude(),
                addressBookMapper,
                addressMapper,
                addressBookDao
        );
    }
}


