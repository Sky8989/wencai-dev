package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.tools.sf.SFExpressHelper;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.enumeration.express.PackageType;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.*;
import com.sftc.web.model.dto.PackageMessageDTO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderSynVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_CONSTANTS_URL;
import static com.sftc.tools.constant.SFConstant.SF_ORDER_SYNC_URL;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;

@Component
public class OrderDetailLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GiftCardMapper giftCardMapper;
    @Resource
    private MessageMapper messageMapper;

    private static final String CONSTANTS_STR = "BASICDATA";

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {

        String order_id = (String) request.getParameter("order_id");
        if (order_id == null || order_id.length() == 0)
            return APIUtil.paramErrorResponse("order_id不能为空");

        OrderDTO orderDTO1 = orderMapper.selectOrderDetailByOrderId(order_id);
        if (orderDTO1 == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        APIResponse apiResponse = syncOrderExpress(order_id);
        if (apiResponse != null) return apiResponse;

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);
        JSONObject respObject = new JSONObject();
        if (orderDTO == null) return APIUtil.getResponse(SUCCESS, null);
        List<OrderExpressDTO> orderExpress = orderDTO.getOrderExpressList();
        for (OrderExpressDTO orderExpressDTO : orderExpress) {
            User receiver = userMapper.selectUserByUserId(orderExpressDTO.getShip_user_id());
            if (receiver != null && receiver.getAvatar() != null) {
                // 扩展收件人头像
                orderExpressDTO.setShip_avatar(receiver.getAvatar());
            }
        }
        setPackageType(orderDTO); //获取包裹信息
        // order
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String resultJson = new Gson().toJson(orderDTO);
        Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        User sender = userMapper.selectUserByUserId(orderDTO.getSender_user_id());
        orderMap.put("sender_avatar", sender.getAvatar());

        // giftCard
        GiftCard giftCard = giftCardMapper.selectGiftCardById(orderDTO.getGift_card_id());
        respObject.put("order", orderMap);
        respObject.put("giftCard", giftCard);

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 订单快递详情
     */
    public APIResponse selectExpressDetail(APIRequest request) {

        // Param
        String uuid = (String) request.getParameter("uuid");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid不能为空");
        // order
        OrderDTO order = orderMapper.selectOrderDetailByUuid(uuid);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        JSONObject respObject = new JSONObject();

        // 同城订单需要access_token
        String access_token = COMMON_ACCESSTOKEN;
        respObject = SFExpressHelper.getExpressDetail(uuid, access_token);
        //处理错误信息
        if (respObject.containsKey("error") || respObject.containsKey("errors") || respObject.containsKey("ERROR")) {
            return APIUtil.selectErrorResponse("查询失败", respObject);
        }

        String order_status = respObject.getJSONObject("request").getString("status");
        String directed_code = null;
        int is_directed = respObject.getJSONObject("request").getString("type").equals("DIRECTED") ? 1 : 0;

        if (respObject.containsKey("request")) {
            JSONObject req = respObject.getJSONObject("request");
            if (req != null && req.containsKey("attributes")) {
                JSONObject attributuOBJ = req.getJSONObject("attributes");
                if (attributuOBJ != null) {
                    if (attributuOBJ.containsKey("directed_code")) {
                        directed_code = attributuOBJ.getString("directed_code");
                        is_directed = 1;
                    }
                }
            }
        }
        // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER
        boolean payed = respObject.getJSONObject("request").getBoolean("payed");
        if (payed && order_status.equals("PAYING") && order.getPay_method().equals("FREIGHT_PREPAID")) {
            order_status = "WAIT_HAND_OVER";
            respObject.getJSONObject("request").put("status", "WAIT_HAND_OVER");
        }
        orderExpressMapper.updateExpressDirectedByUUID(uuid, order_status, directed_code, is_directed);

        APIResponse apiResponse = syncOrderExpress(order.getId());
        if (apiResponse != null) return apiResponse;
        order = orderMapper.selectOrderDetailByUuid(uuid);
        setPackageType(order);//获取包裹信息

        respObject.put("order", order);

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 纯走B端的同城订单详情查询，封装使用公共token
     */
    public APIResponse selectSameExpressDetail(APIRequest request) {
        // Param
        String uuid = (String) request.getParameter("uuid");
        if (!(uuid != null && !uuid.equals(""))) {
            return APIUtil.paramErrorResponse("Parameter uuid missing.");
        }
        JSONObject respObject = SFExpressHelper.getExpressDetail(uuid, COMMON_ACCESSTOKEN);
        if (respObject.containsKey("error")) {
            return APIUtil.selectErrorResponse("查询失败", respObject.getJSONObject("error"));
        }
        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 同步基础数据，返回包裹信息
     */
    private APIResponse setPackageType(OrderDTO order) {
        List<OrderExpressDTO> orderExpressList = order.getOrderExpressList();
        String access_token = COMMON_ACCESSTOKEN;
        if (order.getLatitude() == 0 || order.getLongitude() == 0) {
            Map<String, String> map = new HashMap<>();
            map.put("reason", "寄件人经纬度参数缺失");
            return APIUtil.submitErrorResponse("寄件人经纬度参数缺失", map);
        }
        for (OrderExpressDTO orderExpressDTO : orderExpressList) {  //获取订单基础数据
            String constantsUrl = SF_CONSTANTS_URL + CONSTANTS_STR + "?latitude="
                    + order.getLatitude() + "&longitude=" + order.getLongitude();
            HttpGet get = new HttpGet(constantsUrl);
            get.addHeader("PushEnvelope-Device-Token", access_token);
            String res = APIGetUtil.get(get);
            JSONObject jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("errors") != null || jsonObject.get("error") != null)
                return APIUtil.submitErrorResponse("获取订单基础数据失败", jsonObject);

            try {
                if (jsonObject.getJSONObject("constant").getJSONObject("value").containsKey("PACKAGE_TYPE")) {
                    JSONArray packageTypeArr = jsonObject.getJSONObject("constant").getJSONObject("value").getJSONArray("PACKAGE_TYPE");
                    PackageMessageDTO packageMessage = new PackageMessageDTO();
                    for (int i = 0; i < packageTypeArr.size(); i++) {
                        JSONObject packageTypeOBJ = packageTypeArr.getJSONObject(i);
                        String package_code = packageTypeOBJ.getString("code");
                        if (package_code != null && package_code.equals(orderExpressDTO.getObject_type())) {  //获取物品类型与快递相同的数组
                            JSONArray weightArr = packageTypeOBJ.getJSONArray("weight_segment");
                            for (int j = 0; j < weightArr.size(); j++) {
                                JSONObject weightOBJ = weightArr.getJSONObject(j);
                                //根据包裹大小类型添加包裹信息    0/1/2/3 4 5 --对应-- 小/中/大/超大
                                if (j == 0 && orderExpressDTO.getPackage_type().equals(PackageType.SMALl_PACKAGE.getKey())) {
                                    packageMessage.setName(weightOBJ.getString("name"));
                                    packageMessage.setWeight(weightOBJ.getString("weight"));
                                    packageMessage.setType(PackageType.SMALl_PACKAGE.getKey());
                                }
                                if (j == 1 && orderExpressDTO.getPackage_type().equals(PackageType.CENTRN_PACKAGE.getKey())) {
                                    packageMessage.setName(weightOBJ.getString("name"));
                                    packageMessage.setWeight(weightOBJ.getString("weight"));
                                    packageMessage.setType(PackageType.CENTRN_PACKAGE.getKey());
                                }
                                if (j == 2 && orderExpressDTO.getPackage_type().equals(PackageType.BIG_PACKAGE.getKey())) {
                                    packageMessage.setName(weightOBJ.getString("name"));
                                    packageMessage.setWeight(weightOBJ.getString("weight"));
                                    packageMessage.setType(PackageType.BIG_PACKAGE.getKey());
                                }
                                if (j == 3 || j == 4 || j == 5 && orderExpressDTO.getPackage_type().equals(PackageType.HUGE_PACKAGE.getKey())) {
                                    packageMessage.setName(weightOBJ.getString("name"));
                                    packageMessage.setWeight(weightOBJ.getString("weight"));
                                    packageMessage.setType(PackageType.HUGE_PACKAGE.getKey());
                                }
                            }
                        }
                    }
                    orderExpressDTO.setPackageMessage(packageMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //同步同城的订单信息
    private APIResponse syncOrderExpress(String orderid) {
        // handle url
        String ORDERS_URL = SF_ORDER_SYNC_URL;
        String uuids = "";

        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(orderid);

        for (OrderExpress oe : orderExpressList) { //此处订单若过多 url过长 sf接口会报414
            Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
            if (order != null && oe.getOrder_number() != null && !oe.getOrder_number().equals("")) {
                // 只有同城的订单能同步快递状态
                uuids = uuids + oe.getUuid() + ",";
            }
            if (oe.getOrder_number() == null || oe.getOrder_number().equals("")) {
                orderExpressMapper.updatePayState("WAIT_PAY", oe.getUuid());
            }
        }

        User user = userMapper.getUuidAndtoken(orderid);

        if (uuids.equals("")) return null; //无需同步的订单 直接返回
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("{uuid}", uuids);

        // POST
        List<OrderSynVO> orderSynVOS = null;
        try {
            String token = user.getToken().getAccess_token();
            if (token != null && !token.equals("")) {
                token = user.getToken().getAccess_token();
            } else {
                token = COMMON_ACCESSTOKEN;
            }
            orderSynVOS = APIResolve.getOrderStatusWithUrl(ORDERS_URL, token);
        } catch (Exception e) {
            return APIUtil.submitErrorResponse("正在同步订单状态，稍后重试", e);
        }

        // update express status
        if (orderSynVOS != null) {
            for (OrderSynVO orderSynVO : orderSynVOS) {
                String uuid = orderSynVO.getUuid();
                Order order = orderMapper.selectOrderDetailByUuid(orderSynVO.getUuid());
                String order_status = (orderSynVO.isPayed() && orderSynVO.getStatus().equals("PAYING") &&
                        order.getPay_method().equals("FREIGHT_PREPAID")) ? "WAIT_HAND_OVER" : orderSynVO.getStatus();
                if (order.getPay_method().equals("FREIGHT_COLLECT")) { //到付订单的状态处理
                    order_status = (orderSynVO.getStatus().equals("PAYING")) ? "DELIVERING" : orderSynVO.getStatus();
                }
                String pay_state = orderSynVO.isPayed() ? "ALREADY_PAY" : "WAIT_PAY";
                //存在锁的问题，修改语句改为一条
                JSONObject attributesOBJ = orderSynVO.getAttributes();
                if (attributesOBJ.containsKey("abnormal_option")) {
                    String abNormalError = attributesOBJ.getString("abnormal_option");
                    if (abNormalError != null && abNormalError.equals("CUSTOMER_CANCEL") ||
                            abNormalError.equals("CONTACT_CUSTOMER_FAILURE") || abNormalError.equals("ERROR_CUSTOMER_ADDRESS") ||
                            abNormalError.equals("CONFORM_TO_ORDER_FAILURE") || abNormalError.equals("PICK_UP_OTHERS") ||
                            abNormalError.equals("DISPATCH_TIME_OUT")) {
                        order_status = "CANCELED";
                    } else if (abNormalError != null && abNormalError.equals("CUSTOMER_REJECTION") ||
                            abNormalError.equals("CONTACT_COURIER_FAILURE") || abNormalError.equals("CONTACT_RECEIVER_FAILURE") ||
                            abNormalError.equals("ERROR_RECEIVER_ADDRESS") || abNormalError.equals("TO_DROP_OFF_OTHERS") ||
                            abNormalError.equals("PAY_FAILURE") || abNormalError.equals("VERIFY_FAILURE")) {
                        order_status = "DELIVERING";
                    } else if (abNormalError != null && abNormalError.equals("UNABLE_TO_PICK_UP") ||
                            abNormalError.equals("DISPATCH_FAILED") || abNormalError.equals("DISCARD_TRIP_GROUP")) {
                        order_status = "WAIT_HAND_OVER";
                    }
                }
                String attributes = attributesOBJ.toString();
                orderExpressMapper.updateAttributesAndStatusByUUID(uuid, attributes, order_status, pay_state);

            }
        }
        return null;
    }

    //////////////////////消除 收到地址 的通知消息//////////////////////
    private void remarkMessageReceiveAddress(List<OrderExpress> orderExpressList, int user_id) {
        List<Message> messageReceiveAddress = messageMapper.selectMessageReceiveAddress(user_id);
        //如果生成了“收到地址”通知消息
        if (messageReceiveAddress.size() > 0) {
            boolean flag = false;
            // 查看生成消息的快递id 是否和所查询快递id的相同
            for (OrderExpress orderExpress : orderExpressList) {
                if (orderExpress.getId() == messageReceiveAddress.get(0).getExpress_id()) {
                    flag = true;
                }
            }
            if (flag) messageMapper.updateIsRead(messageReceiveAddress.get(0).getId());
        }
    }

}
