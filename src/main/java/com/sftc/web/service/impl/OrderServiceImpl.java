package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.apiCallback.OrderFriendCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.Address;
import com.sftc.web.model.sfmodel.*;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.*;

import static com.sftc.tools.api.APIConstant.*;
import static com.sftc.tools.api.APIStatus.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Gson gson = new Gson();

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private EvaluateMapper evaluateMapper;
    @Resource
    private GiftCardMapper giftCardMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private UserContactMapper userContactMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private AddressHistoryMapper addressHistoryMapper;

    /**
     * 普通订单提交
     */
    public APIResponse normalOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);
        if (requestObject.containsKey("request")) { // 同城
            return normalSameOrderCommit(requestBody);
        } else { // 大网
            return normalNationOrderCommit(requestBody);
        }
    }

    /**
     * 好友订单提交
     */
    public APIResponse friendOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);
        if (requestObject.containsKey("request")) { // 同城
            return friendSameOrderCommit(requestObject);
        } else { // 大网
            return friendNationOrderCommit(requestObject);
        }
    }

    // 好友同城订单提交
    private APIResponse friendSameOrderCommit(JSONObject requestObject) {
        // Param
        int order_id = ((Double) requestObject.getJSONObject("order").get("order_id")).intValue();
        if (order_id < 0)
            return APIUtil.paramErrorResponse("order_id不能为空");
        if (!requestObject.getJSONObject("order").containsKey("reserve_time"))
            return APIUtil.paramErrorResponse("预约时间不能为空");

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        for (OrderExpress oe : order.getOrderExpressList()) {
            // 拼接同城订单参数中的 source 和 target
            Source source = new Source();
            Address address = new Address();
            address.setProvince(order.getSender_province());
            address.setCity(order.getSender_city());
            address.setRegion(order.getSender_area());
            address.setStreet(order.getSender_addr());
            address.setReceiver(order.getSender_name());
            address.setMobile(order.getSender_mobile());
            Coordinate coordinate = new Coordinate();
            coordinate.setLongitude(order.getLongitude());
            coordinate.setLatitude(order.getLatitude());
            source.setAddress(address);
            source.setCoordinate(coordinate);

            Target target = new Target();
            Address targetAddress = new Address();
            targetAddress.setProvince(oe.getShip_province());
            targetAddress.setCity(oe.getShip_city());
            targetAddress.setRegion(oe.getShip_area());
            targetAddress.setStreet(oe.getShip_addr());
            targetAddress.setReceiver(oe.getShip_name());
            targetAddress.setMobile(oe.getShip_mobile());
            Coordinate targetCoordinate = new Coordinate();
            targetCoordinate.setLongitude(oe.getLongitude());
            targetCoordinate.setLatitude(oe.getLatitude());
            target.setAddress(targetAddress);
            target.setCoordinate(targetCoordinate);

            requestObject.getJSONObject("request").put("source", source);
            requestObject.getJSONObject("request").put("target", target);

            /// Request
            Object tempObj = JSONObject.toBean(requestObject);
            // tempJsonObj 是为了保证对顺丰接口的请求体的完整，不能包含其它的键值对，例如接口的请求参数"order"
            JSONObject tempJsonObj = JSONObject.fromObject(tempObj);
            tempJsonObj.remove("order");
            // Param
            String paramStr = gson.toJson(JSONObject.fromObject(tempJsonObj));
            // POST
            HttpPost post = new HttpPost(SF_REQUEST_URL);
            String token = (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token");
            post.addHeader("PushEnvelope-Device-Token", token);
            String resultStr = AIPPost.getPost(paramStr, post);
            JSONObject responseObject = JSONObject.fromObject(resultStr);

            if (!responseObject.containsKey("error")) {
                String uuid = (String) responseObject.getJSONObject("request").get("uuid");
                /// 数据库操作
                // 订单表更新订单区域类型
                orderMapper.updateOrderRegionType(order_id, "REGION_SAME");
                // 快递表更新uuid和预约时间
                orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time);
                // 消息通知表插入或者更新消息
                List<Message> messageList = messageMapper.selectMessageReceiveExpress(oe.getShip_user_id());
                if (messageList.isEmpty()) {
                    Message message = new Message("RECEIVE_EXPRESS", 0, oe.getId(), oe.getShip_user_id());
                    messageMapper.insertMessage(message);
                } else {
                    Message message = messageList.get(0);
                    message.setIs_read(0);
                    message.setExpress_id(oe.getId());
                    message.setCreate_time(Long.toString(System.currentTimeMillis()));
                    messageMapper.updateMessageReceiveExpress(message);
                }
                // 插入地址
                setupAddress(order, oe);

            } else { // error
                return APIUtil.getResponse(SUBMIT_FAIL, responseObject);
            }
        }

        order = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, order);
    }

    // 好友大网订单提交
    private APIResponse friendNationOrderCommit(JSONObject requestObject) {
        // handle param
        int order_id = ((Double) requestObject.getJSONObject("order").get("order_id")).intValue();
        if (order_id < 0)
            return APIUtil.paramErrorResponse("order_id不能为空");
        if (!requestObject.getJSONObject("order").containsKey("reserve_time"))
            return APIUtil.paramErrorResponse("预约时间不能为空");

        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        for (OrderExpress oe : order.getOrderExpressList()) {
            // 拼接大网订单地址参数
            JSONObject sf = requestObject.getJSONObject("sf");
            sf.put("orderid", oe.getOrder_number());
            sf.put("j_contact", order.getSender_name());
            sf.put("j_mobile", order.getSender_mobile());
            sf.put("j_tel", order.getSender_mobile());
            sf.put("j_country", "中国");
            sf.put("j_province", order.getSender_province());
            sf.put("j_city", order.getSender_city());
            sf.put("j_county", order.getSender_area());
            sf.put("j_address", order.getSender_addr());
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
                // 获取订单结果
                String paramStr = gson.toJson(JSONObject.fromObject(sf));
                HttpPost post = new HttpPost(SF_CREATEORDER_URL);
                post.addHeader("Authorization", "bearer " + APIGetToken.getToken());
                String resultStr = AIPPost.getPost(paramStr, post);
                JSONObject jsonObject = JSONObject.fromObject(resultStr);
                String messageType = (String) jsonObject.get("Message_Type");

                if (messageType != null && messageType.contains("ERROR")) {
                    return APIUtil.submitErrorResponse("下单失败", jsonObject);
                } else {
                    // 存储订单信息
                    String uuid = (String) jsonObject.get("ordernum");
                    String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
                    orderMapper.updateOrderRegionType(order_id, "REGION_NATION");
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time); // 快递表更新uuid和预约时间

                    // 插入地址
                    setupAddress(order, oe);

                    // 存储好友关系
                    UserContactNew userContactNewParam = new UserContactNew();
                    userContactNewParam.setUser_id(order.getSender_user_id());
                    userContactNewParam.setFriend_id(oe.getShip_user_id());
                    UserContactNew userContactNew = userContactMapper.selectByUserIdAndShipId(userContactNewParam);
                    if (userContactNew == null) {
                        userContactNew = new UserContactNew();
                        userContactNew.setUser_id(order.getSender_user_id());
                        userContactNew.setFriend_id(oe.getShip_user_id());
                        userContactNew.setIs_tag_star(0);
                        userContactNew.setLntimacy(0);
                        userContactNew.setCreate_time(Long.toString(System.currentTimeMillis()));
                        userContactMapper.insertUserContact(userContactNew);
                    }

                    //添加 通知信息 RECEIVE_EXPRESS 生成收件人的通知信息
                    //先查 再新建或者更新
                    List<Message> messageList = messageMapper.selectMessageReceiveExpress(oe.getShip_user_id());
                    if (messageList.isEmpty()) {
                        Message message = new Message("RECEIVE_EXPRESS", 0, oe.getId(), oe.getShip_user_id());
                        messageMapper.insertMessage(message);
                    } else {
                        Message message = messageList.get(0);
                        message.setExpress_id(oe.getId());
                        message.setIs_read(0);
                        message.setCreate_time(Long.toString(System.currentTimeMillis()));
                        messageMapper.updateMessageReceiveExpress(message);
                    }
                }
            }
        }
        order = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, order);
    }

    // 订单提交接口验参
    private String orderCommitVerify(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
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

    // 普通同城订单提交
    private APIResponse normalSameOrderCommit(Object object) {

        JSONObject reqObject = JSONObject.fromObject(object);

        if (reqObject.getJSONObject("order").get("reserve_time") != null)
            return APIUtil.paramErrorResponse("预约时间不能为空");

        String order_number = APIRandomUtil.getRandomString();
        APIStatus status = SUCCESS;

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                order_number,
                (String) reqObject.getJSONObject("request").get("pay_type"),
                (String) reqObject.getJSONObject("request").get("product_type"),
                0.0,
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),
                (Double) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("coordinate").get("longitude"),
                (Double) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("coordinate").get("latitude"),
                "ORDER_BASIS",
                Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id"))
        );
        order.setImage((String) reqObject.getJSONObject("order").get("image"));
        order.setVoice((String) reqObject.getJSONObject("order").get("voice"));
        order.setWord_message((String) reqObject.getJSONObject("order").get("word_message"));
        order.setGift_card_id(Integer.parseInt((String) reqObject.getJSONObject("order").get("gift_card_id")));
        order.setVoice_time(Integer.parseInt((String) reqObject.getJSONObject("order").get("voice_time")));
        order.setRegion_type("REGION_SAME");

        HttpPost post = new HttpPost(SF_REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token", (String) reqObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
        JSONObject tempObject = JSONObject.fromObject(object);
        tempObject.remove("order");
        String requestSFParamStr = gson.toJson(tempObject);
        JSONObject respObject = JSONObject.fromObject(AIPPost.getPost(requestSFParamStr, post));

        if (respObject.get("errors") == null || respObject.get("error") == null) {
            // 插入订单表
            orderMapper.addOrder(order);

            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    order_number,
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("receiver"),
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("mobile"),
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("province"),
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("city"),
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("region"),
                    (String) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("street"),
                    (String) reqObject.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("weight"),
                    (String) reqObject.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("type"),
                    "",
                    Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")),
                    order.getId(),
                    (String) respObject.getJSONObject("request").get("uuid"),
                    (Double) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                    (Double) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude")
            );
            orderExpress.setReserve_time((String) reqObject.getJSONObject("order").get("reserve_time"));
            orderExpressMapper.addOrderExpress(orderExpress);

            // 插入地址
            setupAddress(order, orderExpress);

            // 返回结果添加订单编号
            respObject.put("order_id", order.getId());

        } else {
            status = SUBMIT_FAIL;
        }

        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 普通大网下单
     */
    private APIResponse normalNationOrderCommit(Object object) {

        String orderId = APIRandomUtil.getRandomString();
        APIStatus status = SUCCESS;

        JSONObject requestObject = JSONObject.fromObject(object);
        JSONObject orderObject = requestObject.getJSONObject("order");
        JSONObject sf = requestObject.getJSONObject("sf");

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

        String str = gson.toJson(requestObject.getJSONObject("sf"));

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                orderId,
                (String) sf.get("pay_method"),
                "",
                0,
                (String) sf.get("j_contact"),
                (String) sf.get("j_tel"),
                (String) sf.get("j_province"),
                (String) sf.get("j_city"),
                (String) sf.get("j_county"),
                (String) sf.get("j_address"),
                0,
                0,
                "ORDER_BASIS",
                Integer.parseInt((String) orderObject.get("sender_user_id"))
        );
        order.setImage((String) orderObject.get("image"));
        order.setVoice((String) orderObject.get("voice"));
        order.setWord_message((String) orderObject.get("word_message"));
        order.setGift_card_id(Integer.parseInt((String) orderObject.get("gift_card_id")));
        order.setVoice_time(Integer.parseInt((String) orderObject.get("voice_time")));
        order.setRegion_type("REGION_NATION");

        // POST
        HttpPost post = new HttpPost(SF_CREATEORDER_URL);
        post.addHeader("Authorization", "bearer " + APIGetToken.getToken());
        String res = AIPPost.getPost(str, post);
        JSONObject responseObject = JSONObject.fromObject(res);

        if (responseObject.get("Message_Type") != null) {
            if (responseObject.get("Message_Type").equals("ORDER_CREATE_ERROR"))
                status = SUBMIT_FAIL;
        } else {
            // 插入订单表
            orderMapper.addOrder(order);

            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    orderId,
                    (String) sf.get("d_contact"),
                    (String) sf.get("d_tel"),
                    (String) sf.get("d_province"),
                    (String) sf.get("d_city"),
                    (String) sf.get("d_county"),
                    (String) sf.get("d_address"),
                    "",
                    "",
                    "INIT",
                    Integer.parseInt((String) orderObject.get("sender_user_id")),
                    order.getId(),
                    orderId,
                    0.0,
                    0.0
            );
            orderExpress.setReserve_time("");
            orderExpressMapper.addOrderExpress(orderExpress);

            // 插入地址
            setupAddress(order, orderExpress);

            // 返回结果添加订单编号
            responseObject.put("order_id", order.getId());
        }

        return APIUtil.getResponse(status, responseObject);
    }

    // 插入地址
    private void setupAddress(Order order, OrderExpress oe) {
        com.sftc.web.model.Address address = new com.sftc.web.model.Address(oe);
        addressMapper.addAddress(address);
        // 插入历史地址
        AddressHistory addressHistory = new AddressHistory();
        addressHistory.setUser_id(order.getSender_user_id());
        addressHistory.setAddress_id(address.getId());
        addressHistory.setCreate_time(Long.toString(System.currentTimeMillis()));
        addressHistory.setIs_delete(0);
        int is_mystery = order.getOrder_type().equals("ORDER_MYSTERY") ? 1 : 0;
        addressHistory.setIs_mystery(is_mystery);
        addressHistoryMapper.insertAddressHistory(addressHistory);
    }

    /**
     * 计价
     */
    public APIResponse countPrice(Object object) {

        JSONObject jsonObject = JSONObject.fromObject(object);
        HttpPost post = new HttpPost(SF_QUOTES_URL);
        String uuid = (String) jsonObject.getJSONObject("request").getJSONObject("merchant").get("uuid");
        String access_token = (String) jsonObject.getJSONObject("request").getJSONObject("token").get("access_token");

        boolean uuidFlag = (uuid != null) && !(uuid.equals(""));
        boolean tokenFlag = (access_token != null) && !(access_token.equals(""));

        if ((uuidFlag && !tokenFlag) || (!uuidFlag && tokenFlag))
            return APIUtil.paramErrorResponse("uuid 和 access_token 不能只传一个");

        if (tokenFlag) {
            post.addHeader("PushEnvelope-Device-Token", access_token);
        } else {
            // 下单时，如果还没登录，计价时uuid和token都没有，需要先写死
            jsonObject.getJSONObject("request").getJSONObject("merchant").put("uuid", "2c9a85895c352c20015c3878647b017a");
            post.addHeader("PushEnvelope-Device-Token", "padHjjRvusAC9z7ehxpG");
        }

        String res = AIPPost.getPost(gson.toJson(jsonObject), post);
        JSONObject respObject = JSONObject.fromObject(res);

        APIStatus status = respObject.get("error") == null ? SUCCESS : QUOTE_FAIL;

        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {

        String token = (String) request.getParameter("token");
        String uuid = (String) request.getParameter("uuid");
        String access_token = (String) request.getParameter("access_token");

        int user_id = tokenMapper.selectUserIdByToken(token);
        User user = userMapper.selectUserByUserId(user_id);

        if (token == null || token.equals(""))
            return APIUtil.paramErrorResponse("token无效");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid无效");
        if (access_token == null || access_token.equals(""))
            return APIUtil.paramErrorResponse("access_token无效");

        String pay_url = SF_REQUEST_URL + "/" + uuid + "/js_pay?open_id=" + user.getOpen_id();
        HttpPost post = new HttpPost(pay_url);
        post.addHeader("PushEnvelope-Device-Token", access_token);
        String res = AIPPost.getPost("", post);
        JSONObject jsonObject = JSONObject.fromObject(res);

        return APIUtil.getResponse(SUCCESS, jsonObject);
    }

    /**
     * 寄件人填写订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {

        OrderParam orderParam = (OrderParam) request.getRequestParam();

        // 插入订单表
        Order order = new Order(orderParam);
        String order_number = APIRandomUtil.getRandomString();
        order.setOrder_number(order_number);
        orderMapper.addOrder(order);

        // 插入快递表
        OrderExpress orderExpress = new OrderExpress();
        orderExpress.setPackage_type(orderParam.getPackage_type());
        orderExpress.setObject_type(orderParam.getObject_type());
        orderExpress.setOrder_id(order.getId());
        orderExpress.setCreate_time(order.getCreate_time());
        orderExpress.setState("WAIT_FILL");
        orderExpress.setUuid("");
        orderExpress.setSender_user_id(orderParam.getSender_user_id());
        orderExpress.setReserve_time("");
        orderExpress.setOrder_id(order.getId());
        for (int i = 0; i < orderParam.getPackage_count(); i++) {
            orderExpress.setOrder_number(APIRandomUtil.getRandomString());
            orderExpressMapper.addOrderExpress(orderExpress);
        }

        // 插入地址表
        com.sftc.web.model.Address senderAddress = new com.sftc.web.model.Address(orderParam);
        addressMapper.addAddress(senderAddress);

        return APIUtil.getResponse(SUCCESS, order.getId());
    }

    /**
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(Map rowData) {

        String orderExpressStr = rowData.toString();
        OrderExpress orderExpress = new Gson().fromJson(orderExpressStr, OrderExpress.class);

        // 判断订单是否下单
        Order order = orderMapper.selectOrderDetailByOrderId(orderExpress.getOrder_id());
        if (order.getRegion_type() != null && !"".equals(order.getRegion_type()) && order.getRegion_type().length() != 0) {
            return APIUtil.submitErrorResponse("订单已经下单，现在您无法再填写信息", orderExpress.getOrder_id());
        }

        // 判断同一个用户重复填写
        LinkedList<OrderExpress> realList = new LinkedList<OrderExpress>();
        List<OrderExpress> preList = orderExpressMapper.findAllOrderExpressByOrderId(orderExpress.getOrder_id());
        for (OrderExpress oe : preList) {
            if (oe.getShip_user_id() == orderExpress.getShip_user_id())
                return APIUtil.submitErrorResponse("您已经填写过信息，请勿重复填写", orderExpress.getOrder_id());
            if (oe.getShip_user_id() == 0)
                realList.add(oe);
        }

        if (realList.isEmpty()) { // 已抢完
            return APIUtil.getResponse(APIStatus.ORDER_PACKAGE_COUNT_PULL, null);
        } else {
            // 随机获取包裹编号
            int random = new Random().nextInt(realList.size());
            orderExpress.setState("ALREADY_FILL");
            orderExpress.setId(realList.get(random).getId());
            orderExpress.setReceive_time(Long.toString(System.currentTimeMillis()));
            orderExpressMapper.updateOrderExpressByOrderExpressId(orderExpress);

            // 添加RECEIVE_ADDRESS通知消息，此时应该是寄件人收到通知
            List<Message> messageList = messageMapper.selectMessageReceiveAddress(order.getSender_user_id());
            if (messageList.isEmpty() && messageList.size() == 0) {
                Message message = new Message("RECEIVE_ADDRESS", 0, orderExpress.getId(), order.getSender_user_id());
                messageMapper.insertMessage(message);
            } else {
                Message message = messageList.get(0);
                message.setIs_read(0);
                message.setExpress_id(orderExpress.getId());
                message.setUser_id(order.getSender_user_id());
                message.setCreate_time(Long.toString(System.currentTimeMillis()));
                messageMapper.updateMessageReceiveAddress(message);
            }
        }

        return APIUtil.getResponse(SUCCESS, orderExpress.getOrder_id());
    }

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {

        String order_number = (String) request.getParameter("order_number");
        String order_id = (String) request.getParameter("order_id");

        Order order;
        if (order_number != null && order_number.length() != 0) {
            order = orderMapper.selectOrderDetailByOrderNumber(order_number);
        } else if (order_id != null && order_id.length() != 0) {
            order = orderMapper.selectOrderDetailByOrderId(Integer.parseInt(order_id));
        } else {
            return APIUtil.paramErrorResponse("order_number order_id 必须传一个");
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (order == null) return APIUtil.getResponse(SUCCESS, null);

        for (OrderExpress oe : order.getOrderExpressList()) {
            User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
            if (receiver != null && receiver.getAvatar() != null) {
                // 扩展收件人头像
                oe.setShip_avatar(receiver.getAvatar());
            }
        }

        // order
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String resultJson = new Gson().toJson(order);
        Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        User sender = userMapper.selectUserByUserId(order.getSender_user_id());
        orderMap.put("sender_avatar", sender.getAvatar());

        // giftCard
        GiftCard giftCard = giftCardMapper.selectGiftCardById(order.getGift_card_id());

        resultMap.put("order", orderMap);
        resultMap.put("giftCard", giftCard);

        return APIUtil.getResponse(SUCCESS, resultMap);
    }

    /**
     * 快递详情接口
     */
    public APIResponse sfOrderDetail(int order_id, String access_token, String uuid) {

        if (uuid == null)
            uuid = orderExpressMapper.getUuidByOrderId(order_id);

        JSONObject jsonObject = APISfDetail.sfDetail(uuid, access_token);

        return APIUtil.getResponse(SUCCESS, jsonObject);
    }

    /**
     * 更改订单状态
     */
    public APIResponse updateOrderStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        int order_id = ((Double) requestObject.get("order_id")).intValue();
        String status = (String) requestObject.get("status");
        if (order_id < 1)
            return APIUtil.paramErrorResponse("参数order_id不能为空");
        if (status == null || status.equals(""))
            return APIUtil.paramErrorResponse("参数status不能为空");
        if (!(status.equals("WAIT_FILL") ||
                status.equals("ALREADY_FILL") ||
                status.equals("INIT") ||
                status.equals("PAYING") ||
                status.equals("WAIT_HAND_OVER") ||
                status.equals("DELIVERING") ||
                status.equals("FINISHED") ||
                status.equals("ABNORMAL") ||
                status.equals("CANCELED") ||
                status.equals("WAIT_REFUND") ||
                status.equals("REFUNDING") ||
                status.equals("REFUNDED"))) {
            return APIUtil.paramErrorResponse("参数status不正确");
        }
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (order == null)
            return APIUtil.submitErrorResponse("订单不存在", null);

        // update
        for (OrderExpress oe : order.getOrderExpressList()) {
            orderExpressMapper.updateOrderExpressStatus(oe.getId(), status);
        }
        order = orderMapper.selectOrderDetailByOrderId(order_id);
        return APIUtil.getResponse(SUCCESS, order);
    }

    /**
     * 返回未被填写的包裹
     */
    public APIResponse getEmptyPackage(APIRequest request) {

        String order_number = (String) request.getParameter("order_number");

        List<OrderExpress> orderExpressList = orderExpressMapper.findEmptyPackage(order_number);
        int randomOrderExpress = (int) (Math.random() * orderExpressList.size());
        OrderExpress orderExpress = orderExpressList.get(randomOrderExpress);

        return APIUtil.getResponse(SUCCESS, orderExpress);
    }

    /**
     * 我的订单列表
     */
    public APIResponse getMyOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();

        APIResponse errorResponse = syncSFExpressStatus(myOrderParam);
        if (errorResponse != null) return errorResponse;

        // pageNum -> startIndex
        myOrderParam.setPageNum((myOrderParam.getPageNum() - 1) * myOrderParam.getPageSize());
        // select
        List<Order> orderList = orderMapper.selectMyOrderList(myOrderParam);
        List<OrderCallback> orderCallbacks = new ArrayList<OrderCallback>();
        for (Order order : orderList) {
            OrderCallback callback = new OrderCallback();
            // order
            callback.setId(order.getId());
            callback.setSender_name(order.getSender_name());
            callback.setSender_addr(order.getSender_addr());
            callback.setOrder_number(order.getOrder_number());
            callback.setOrder_type(order.getOrder_type());
            callback.setRegion_type(order.getRegion_type());
            callback.setIs_gift(order.getGift_card_id() > 0);
            // expressList
            List<OrderCallback.OrderCallbackExpress> expressList = new ArrayList<OrderCallback.OrderCallbackExpress>();
            for (OrderExpress oe : order.getOrderExpressList()) {
                OrderCallback.OrderCallbackExpress express = new OrderCallback().new OrderCallbackExpress();
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                express.setShip_addr(oe.getShip_addr());
                expressList.add(express);
            }
            callback.setExpressList(expressList);

            orderCallbacks.add(callback);
        }

        return APIUtil.getResponse(SUCCESS, orderCallbacks);
    }

    /**
     * 我的好友圈订单列表
     */
    public APIResponse getMyFriendCircleOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();

        APIResponse errorResponse = syncSFExpressStatus(myOrderParam);
        if (errorResponse != null) return errorResponse;

        // pageNum -> startIndex
        myOrderParam.setPageNum((myOrderParam.getPageNum() - 1) * myOrderParam.getPageSize());
        // select
        List<Order> orderList = orderMapper.selectMyFriendOrderList(myOrderParam);
        List<OrderFriendCallback> orderCallbacks = new ArrayList<OrderFriendCallback>();
        for (Order order : orderList) {
            OrderFriendCallback callback = new OrderFriendCallback();
            User sender = userMapper.selectUserByUserId(order.getSender_user_id());
            // order
            callback.setId(order.getId());
            callback.setSender_user_id(order.getSender_user_id());
            callback.setSender_name(order.getSender_name());
            if (sender != null && sender.getAvatar() != null) {
                callback.setSender_avatar(sender.getAvatar());
            }
            if (order.getOrderExpressList() != null && order.getOrderExpressList().size() > 0 && order.getOrderExpressList().get(0).getObject_type().length() > 0) { // powerful verify
                callback.setObject_type(order.getOrderExpressList().get(0).getObject_type());
            }
            callback.setWord_message(order.getWord_message());
            callback.setImage(order.getImage());
            callback.setCreate_time(order.getCreate_time());
            callback.setRegion_type(order.getRegion_type());
            callback.setIs_gift(order.getGift_card_id() > 0);
            // expressList
            List<OrderFriendCallback.OrderFriendCallbackExpress> expressList = new ArrayList<OrderFriendCallback.OrderFriendCallbackExpress>();
            for (OrderExpress oe : order.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
                OrderFriendCallback.OrderFriendCallbackExpress express = new OrderFriendCallback().new OrderFriendCallbackExpress();
                express.setId(oe.getId());
                express.setShip_user_id(oe.getShip_user_id());
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                if (receiver != null && receiver.getAvatar() != null)
                    express.setShip_avatar(receiver.getAvatar());
                expressList.add(express);
            }
            callback.setExpressList(expressList);

            orderCallbacks.add(callback);
        }

        return APIUtil.getResponse(SUCCESS, orderCallbacks);
    }

    // 验参、同步顺丰快递订单状态
    private APIResponse syncSFExpressStatus(MyOrderParam myOrderParam) {

        // Verify params
        if (myOrderParam.getToken().length() == 0) {
            return APIUtil.paramErrorResponse("token不能为空");
        } else if (myOrderParam.getId() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (myOrderParam.getPageNum() < 1 || myOrderParam.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        // handle SF orders url
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        StringBuilder uuidSB = new StringBuilder();
        for (OrderExpress oe : orderExpressList) {
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
                if (order.getRegion_type().equals("REGION_SAME")) { // 只有同城的订单能同步快递状态
                    uuidSB.append(oe.getUuid());
                    uuidSB.append(",");
                }
            }
        }
        String uuid = uuidSB.toString();
        if (uuid.equals("")) return null;

        String ordersURL = SF_ORDER_SYNC_URL.replace("{uuid}", uuid.substring(0, uuid.length() - 1));
        List<Orders> ordersList;
        try { // post and fetch express status list
            ordersList = APIResolve.getOrdersJson(ordersURL, myOrderParam.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            return APIUtil.selectErrorResponse(e.getLocalizedMessage(), e);
        }

        // Update Dankal express info
        for (Orders orders : ordersList) {
            orderExpressMapper.updateOrderExpressForSF(new OrderExpress(orders.getStatus(), orders.getUuid()));
        }

        return null;
    }

    /**
     * 好友下单
     */
    public APIResponse friendPlace(Object object) {

        JSONObject requestObject = JSONObject.fromObject(object);

        if (requestObject.getJSONObject("request").getJSONObject("order").get("reserve_time") == null)
            return APIUtil.paramErrorResponse("预约时间不能为空");

        // POST
        HttpPost post = new HttpPost(SF_REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token", (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
        String res = AIPPost.getPost(gson.toJson(object), post);
        JSONObject responseObject = JSONObject.fromObject(res);

        // 插入快递表
        OrderExpress orderExpress = new OrderExpress(
                (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("uuid"),
                Integer.parseInt((String) requestObject.getJSONObject("request").get("order_id")),
                (Double) requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),
                (Double) requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                (String) responseObject.getJSONObject("request").get("status")
        );
        orderExpress.setReserve_time((String) requestObject.getJSONObject("request").getJSONObject("order").get("reserve_time"));
        orderExpressMapper.updatePlace(orderExpress);

        // 插入订单表
        Order order = new Order(
                (Double) requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),
                (Double) requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                Integer.parseInt((String) requestObject.getJSONObject("request").get("order_id"))
        );
        orderMapper.updatePlace(order);

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 订单快递详情
     */
    public APIResponse selectExpressDetail(APIRequest request) {

        APIStatus status = SUCCESS;

        // Param
        String uuid = (String) request.getParameter("uuid");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid不能为空");
        // order
        Order order = orderMapper.selectOrderDetailByUuid(uuid);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        JSONObject respObject = new JSONObject();
        String regionType = order.getRegion_type();
        if (regionType.equals("REGION_NATION")) { // 大网

            OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
            // sort
            String sort = (String) request.getParameter("sort");
            sort = sort == null ? "desc" : sort;
            // GET
            String url = SF_ORDERROUTE_URL + orderExpress.getOrder_number() + "&sort=" + sort;
            HttpGet get = new HttpGet(url);
            String access_token = APIGetToken.getToken();
            get.addHeader("Authorization", "bearer " + access_token);
            String res = APIGet.getGet(get);

            if (!res.equals("[]")) {
                JSONArray routeList = JSONArray.fromObject(res);
                if (routeList != null)
                    respObject.put("sf", routeList);
            } else {
                respObject.put("sf", new JSONObject());
                status = APIStatus.ORDERROUT_FALT;
            }

        } else if (regionType.equals("REGION_SAME")) { // 同城

            // 同城订单需要access_token
            String access_token = (String) request.getParameter("access_token");
            if (access_token == null || access_token.equals(""))
                return APIUtil.paramErrorResponse("access_token不能为空");

            respObject = APISfDetail.sfDetail(uuid, access_token);
        }

        respObject.put("order", order);

        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 未下单详情接口
     */
    public APIResponse noPlaceOrderDetail(int order_id) {
        Order order = orderMapper.orderAndOrderExpressAndGiftDetile(order_id);
        APIStatus status = order == null ? COURIER_NOT_FOUND : SUCCESS;

        return APIUtil.getResponse(status, order);
    }

    /**
     * 订单评价 已废弃
     */
    public APIResponse evaluate(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        // 生成 顺丰订单评价接口 需要的信息
        String str = gson.toJson(object);
        JSONObject jsonObject = null;
        JSONObject jsonObjectParam = JSONObject.fromObject(object);
        JSONObject request = jsonObjectParam.getJSONObject("request");
        JSONObject attributes = jsonObjectParam.getJSONObject("request").getJSONObject("attributes");
        int order_id = request.getInt("order_id");

        Evaluate evaluate = new Evaluate();
        evaluate.setMerchant_comments(attributes.getString("merchant_comments"));
        evaluate.setMerchant_score(attributes.getString("merchant_score"));
        evaluate.setMerchant_tags(attributes.getString("merchant_tags"));
        evaluate.setOrder_id(request.getInt("order_id"));
        evaluate.setUser_id(request.getInt("user_id"));
        evaluate.setCreate_time(Long.toString(System.currentTimeMillis()));

        // 获取 该订单对应的快递信息
        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(order_id);
        StringBuilder uuidStr = new StringBuilder();
        // 定义一个flag 只有存在含有uuid的订单才调用顺丰接口
        boolean flag = false;
        for (OrderExpress oe : orderExpressList) {
            // 循环遍历orderExpressList，对填写过的快递信息，取出uuid，发到顺丰去评价
            if (oe.getUuid() != null && !"".equals(oe.getUuid())) {
                flag = true;
                uuidStr.append(oe.getUuid());
                uuidStr.append(",");
            }
        }
        uuidStr.deleteCharAt(uuidStr.length() - 1);
        if (flag) {
            // 向顺丰的接口发送评价信息
            String evaluate_url = SF_REQUEST_URL + "/" + uuidStr.toString() + "/attributes/merchant_comment";
            HttpPut put = new HttpPut(evaluate_url);
            put.addHeader("PushEnvelope-Device-Token", (String) request.get("access_token"));
            String res = AIPPost.getPost(str, put);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
                status = APIStatus.EVALUATE_FALT;
            } else {// 评价成功后，向评价表存入 评价记录
                jsonObject = jsonObject.getJSONObject("attributes");
                evaluateMapper.addEvaluate(evaluate);
            }
        } else {
            // 当 flag 为false 时，说明没有可评价的订单，也是评价失败
            status = APIStatus.EVALUATE_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 评价某个订单的单一包裹
     */
    public APIResponse evaluateSingle(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        // 生成 顺丰订单评价接口 需要的信息
        String str = gson.toJson(object);
        JSONObject jsonObject = null;
        JSONObject jsonObjectParam = JSONObject.fromObject(object);
        JSONObject request = jsonObjectParam.getJSONObject("request");
        JSONObject attributes = jsonObjectParam.getJSONObject("request").getJSONObject("attributes");
        String uuid = request.getString("uuid");
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        if (orderExpress == null){
            return APIUtil.selectErrorResponse("该uuid无对应快递信息，请检查uuid",request);
        }

        /// 向顺丰的接口发送评价信息
        String evaluate_url = SF_REQUEST_URL + "/" + uuid + "/attributes/merchant_comment";
        HttpPut put = new HttpPut(evaluate_url);
        put.addHeader("PushEnvelope-Device-Token", (String) request.get("access_token"));
        String res = AIPPost.getPost(str, put);
        jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
            status = APIStatus.EVALUATE_FALT;
        } else {
            /// 评价成功后，向评价表存入 评价记录
            Evaluate evaluate = new Evaluate();
            evaluate.setMerchant_comments(attributes.getString("merchant_comments"));
            evaluate.setMerchant_score(attributes.getString("merchant_score"));
            evaluate.setMerchant_tags(attributes.getString("merchant_tags"));
            evaluate.setOrderExpress_id(orderExpress.getUuid());
            evaluate.setUuid(uuid);
            evaluate.setUser_id(request.getInt("user_id"));
            evaluate.setCreate_time(Long.toString(System.currentTimeMillis()));
            // 评价成功后sf返回结果信息较长 截取attributes
            jsonObject = jsonObject.getJSONObject("request").getJSONObject("attributes");
            evaluateMapper.addEvaluate(evaluate);
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 取消订单
     */
    public APIResponse deleteOrder(Object object) {
        APIStatus status = SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        //获取订单id，便于后续取消订单操作的取用
        int id = jsonObject1.getInt("order_id");
        jsonObject1.remove("order_id");
        //对重复取消订单的情况进行处理
        Order order = orderMapper.selectOrderDetailByOrderId(id);
        if ("Cancelled".equals(order.getIs_cancel()) || !"".equals(order.getIs_cancel())) {//is_cancle字段默认是空字符串
            //return APIUtil.getResponse(APIStatus.CANCEL_ORDER_FALT, null);
            return APIUtil.submitErrorResponse("订单已经取消，请勿重复取消操作！！！",null);
        }
        List<OrderExpress> arrayList = orderExpressMapper.findAllOrderExpressByOrderId(id);
        StringBuilder stringBuilder = new StringBuilder();
        //遍历所有的快递列表
        boolean flagRealOrder = false;
        for (OrderExpress eachOrderExpress : arrayList) {
            if (eachOrderExpress.getUuid() != null && !"".equals(eachOrderExpress.getUuid())) {
                flagRealOrder = true;
                stringBuilder.append(eachOrderExpress.getUuid());
                stringBuilder.append(",");
                continue;
            }
            //如果是空订单，就结束此次循环，进行下一次
            System.out.println("-   -未付款订单，订单编号是：" + eachOrderExpress.getOrder_number());
        }
        if (flagRealOrder) {//这是订单已经提交付款了的操作
            //   todo:真实订单还未测试到
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            //下面是 顺丰方面取消订单的逻辑
            String str = "{\"event\":{\"type\":\"CANCEL\",\"source\":\"MERCHANT\"}}";
            //String str = gson.toJson(object);
            String myUrl = SF_REQUEST_URL + "/" + stringBuilder.toString() + "/events";
            System.out.println("-   -这是myurl" + myUrl);
            HttpPost post = new HttpPost(myUrl);
            post.addHeader("PushEnvelope-Device-Token", jsonObject1.getString("access_token"));
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("error") != null) {
                return APIUtil.submitErrorResponse("订单取消失败,sf接口出错", jsonObject);
            } else {
                orderMapper.updateCancelOrderById(id);
                orderExpressMapper.updateOrderExpressCanceled(id);
            }
        } else {//订单还未提交给顺丰的情况，只更新order的信息即可
            orderMapper.updateCancelOrderById(id);
            orderExpressMapper.updateOrderExpressCanceled(id);
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 预约时间规则
     */
    public APIResponse timeConstants(APIRequest request) {
        APIStatus status = SUCCESS;
        String constantsUrl = SF_CONSTANTS_URL + request.getParameter("constants") + "?latitude=" + request.getParameter("latitude") + "&longitude=" + request.getParameter("longitude");
        HttpGet get = new HttpGet(constantsUrl);
        get.addHeader("PushEnvelope-Device-Token", (String) request.getParameter("access_token"));
        String res = APIGet.getGet(get);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
            status = APIStatus.CONSTANT_FALT;
        }

        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 大网计价
     */
    public APIResponse OrderFreightQuery(Object object) {

        APIStatus status = SUCCESS;

        HttpPost post = new HttpPost(SF_COUNT_PRICE);
        String access_token = APIGetToken.getToken();
        post.addHeader("Authorization", "bearer " + access_token);
        String res = AIPPost.getPost(gson.toJson(object), post);
        JSONObject responseObject = JSONObject.fromObject(res);

        if (responseObject.get("Message_Type") != null)
            if (responseObject.get("Message_Type").equals("ORDER_CREATE_ERROR"))
                status = APIStatus.QUOTE_FAIL;

        return APIUtil.getResponse(status, responseObject);
    }

    /**
     * 大网路由
     */
    public APIResponse OrderRouteQuery(APIRequest request) {

        APIStatus status = SUCCESS;
        JSONObject responseObject = null;

        String routeUrl = SF_ORDERROUTE_URL + request.getParameter("orderid") + "&sort=" + request.getParameter("sort");
        HttpGet get = new HttpGet(routeUrl);
        String access_token = APIGetToken.getToken();
        get.addHeader("Authorization", "bearer " + access_token);
        String res = APIGet.getGet(get);

        if (!res.equals("[]")) {
            responseObject = JSONObject.fromObject(res);
            if (responseObject.get("Message_Type") != null) {
                if (responseObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            }
        } else {
            status = APIStatus.ORDERROUT_FALT;
        }

        return APIUtil.getResponse(status, responseObject);
    }

    /**
     * 可支付列表
     */
    public APIResponse remindPlace(APIRequest request) {
        Order order = new Order();
        order.setSender_user_id(Integer.parseInt((String) request.getParameter("sender_user_id")));
        List<Order> orderList = orderMapper.getOrderAndExpress(order);
        return APIUtil.getResponse(SUCCESS, orderList);
    }
}
