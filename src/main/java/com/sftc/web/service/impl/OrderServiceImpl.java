package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;

import com.sftc.web.model.sfmodel.Address;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.apiCallback.OrderFriendCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.*;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final String QUOTES_URL = "http://api-dev.sf-rush.com/quotes";
    private static final String REQUEST_URL = "http://api-dev.sf-rush.com/requests";
    private static final String CONSTANTS_URL = "http://api-dev.sf-rush.com/constants/";

    private static final String CREATEORDER_URL = "http://api-c.sf-rush.com/api/sforderservice/ordercreate";
    private static final String COUNT_PRICE = "http://api-c.sf-rush.com/api/sforderservice/OrderFreightQuery";
    private static final String ORDERROUTE_URL = "http://api-c.sf-rush.com/api/sforderservice/OrderRouteQuery?orderid=";

    private Gson gson = new Gson();

    String time = Long.toString(System.currentTimeMillis());

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
    /**
     * 普通订单提交
     */
    public APIResponse normalOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = normalOrderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.errorResponse(paramVerifyMessage);
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
        String paramVerifyMessage = normalOrderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.errorResponse(paramVerifyMessage);
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
        APIStatus status = APIStatus.SUCCESS;
        int order_id = Integer.parseInt((String) requestObject.getJSONObject("order").get("order_id"));
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

            // POST
            Object tempObj = JSONObject.toBean(requestObject);
            JSONObject tempJsonObj = JSONObject.fromObject(tempObj);
            tempJsonObj.remove("order");
            String paramStr = gson.toJson(JSONObject.fromObject(tempJsonObj));

            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
            String resultStr = AIPPost.getPost(paramStr, post);
            JSONObject jsonObject = JSONObject.fromObject(resultStr);

            if (!jsonObject.containsKey("error")) {
                if (requestObject.getJSONObject("order").containsKey("reserve_time")) {
                    String uuid = (String) jsonObject.getJSONObject("request").get("uuid");
                    String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
                    orderMapper.updateOrderRegionType(order_id, "REGION_SAME");
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time); // 快递表更新uuid和预约时间
                } else {
                    return APIUtil.errorResponse("预约时间不能为空");
                }
            } else {
                status = APIStatus.SUBMIT_FAIL;
            }
        }
        return APIUtil.getResponse(status, null);
    }

    // 好友大网订单提交
    private APIResponse friendNationOrderCommit(JSONObject requestObject) {
        // handle param
        if (!requestObject.getJSONObject("order").containsKey("reserve_time")) {
            return APIUtil.errorResponse("预约时间不能为空");
        }

        APIStatus status = APIStatus.SUCCESS;
        int order_id = Integer.parseInt((String) requestObject.getJSONObject("order").get("order_id"));
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
                // 发送请求给sf，获取订单结果
                String paramStr = gson.toJson(JSONObject.fromObject(sf));
                HttpPost post = new HttpPost(CREATEORDER_URL);
                post.addHeader("Authorization", "bearer " + APIGetToken.getToken());
                String resultStr = AIPPost.getPost(paramStr, post);
                JSONObject jsonObject = JSONObject.fromObject(resultStr);
                String messageType = (String) jsonObject.get("Message_Type");
                if (messageType != null && messageType.contains("ERROR")) {
                    status = APIStatus.SUBMIT_FAIL;
                } else {
                    // 存储订单信息
                    String uuid = (String) jsonObject.get("ordernum");
                    String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
                    orderMapper.updateOrderRegionType(order_id, "REGION_NATION");
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time); // 快递表更新uuid和预约时间

                    //  存储 好友地址 到address表
                    com.sftc.web.model.Address shipAddress = new com.sftc.web.model.Address(oe);
                    addressMapper.addAddress(shipAddress);
                    // 存储 好友关系 到user_contact
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
                        userContactNew.setCreate_time(time);
                        userContactMapper.insertUserContact(userContactNew);
                        //System.out.println("-   -有人成为好朋友了"+userContactNew.toString());
                    }
                }
            }
        }
        return APIUtil.getResponse(status, null);
    }

    // 普通订单提交接口验参
    private String normalOrderCommitVerify(Object object) {
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

    // 普通同城订单
    private APIResponse normalSameOrderCommit(Object object) {

        String order_number = APIRandomUtil.getRandomString();
        APIStatus status = APIStatus.SUCCESS;
        JSONObject respObject = null;
        try {
            JSONObject reqObject = JSONObject.fromObject(object);
            Order order = new Order(
                    time,
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
                    Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")));
            order.setImage((String) reqObject.getJSONObject("order").get("image"));
            order.setVoice((String) reqObject.getJSONObject("order").get("voice"));
            order.setWord_message((String) reqObject.getJSONObject("order").get("word_message"));
            order.setGift_card_id(Integer.parseInt((String) reqObject.getJSONObject("order").get("gift_card_id")));
            order.setVoice_time(Integer.parseInt((String) reqObject.getJSONObject("order").get("voice_time")));
            order.setRegion_type("REGION_SAME");

            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) reqObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
            JSONObject tempObject = JSONObject.fromObject(object);
            tempObject.remove("order");
            String requestSFParamStr = gson.toJson(tempObject);
            respObject = JSONObject.fromObject(AIPPost.getPost(requestSFParamStr, post));
            if (respObject.get("errors") == null || respObject.get("error") == null) {
                if (reqObject.getJSONObject("order").get("reserve_time") != null) {
                    orderMapper.addOrder(order);
                    OrderExpress orderExpress = new OrderExpress(
                            time,
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
                            (Double) reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"));
                    if (reqObject.getJSONObject("order").get("reserve_time") != null) {
                        orderExpress.setReserve_time((String) reqObject.getJSONObject("order").get("reserve_time"));
                    }
                    orderExpressMapper.addOrderExpress(orderExpress);
                }
                respObject.put("order_id", order.getId());
            } else {
                status = APIStatus.SUBMIT_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 普通大网下单
     */
    private APIResponse normalNationOrderCommit(Object object) {
        String orderid = APIRandomUtil.getRandomString();
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
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

        sf.put("orderid", orderid);

        String str = gson.toJson(requestObject.getJSONObject("sf"));
        try {
            Order order = new Order(
                    time,
                    orderid,
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

            //发送请求给sf，获取订单结果
            HttpPost post = new HttpPost(CREATEORDER_URL);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("Message_Type") != null) {
                if (jsonObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.SUBMIT_FAIL;
                }
            } else {
                //存储 订单信息
                orderMapper.addOrder(order);
                OrderExpress orderExpress = new OrderExpress(
                        time,
                        orderid,
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
                        orderid,
                        0.0,
                        0.0
                );
                orderExpress.setReserve_time("");
                //存储 订单快递信息
                orderExpressMapper.addOrderExpress(orderExpress);
                jsonObject.put("order_id", order.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 计价
     */
    public APIResponse countPrice(Object object) {
        Gson gson1 = new Gson();
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        JSONObject jsonObject = JSONObject.fromObject(str);
        HttpPost post = new HttpPost(QUOTES_URL);
        String uuid = (String) jsonObject.getJSONObject("request").getJSONObject("merchant").get("uuid");
        String access_token = (String) jsonObject.getJSONObject("request").getJSONObject("token").get("access_token");

        boolean uuidFlag = (uuid != null) && !(uuid.equals(""));
        boolean tokenFlag = (access_token != null) && !(access_token.equals(""));

        if ((uuidFlag && !tokenFlag) || (!uuidFlag && tokenFlag))
            return APIUtil.errorResponse("uuid 和 access_token 不能只传一个");

        if (tokenFlag) {
            post.addHeader("PushEnvelope-Device-Token", access_token);
        } else {
            jsonObject.getJSONObject("request").getJSONObject("merchant").put("uuid", "2c9a85895c352c20015c3878647b017a");
            post.addHeader("PushEnvelope-Device-Token", "padHjjRvusAC9z7ehxpG");
        }

        String res = AIPPost.getPost(gson1.toJson(jsonObject), post);
        JSONObject respObject = JSONObject.fromObject(res);

        if (respObject.get("error") != null) {
            status = APIStatus.QUOTE_FAIL;
        }

        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            String token = (String) request.getParameter("token");
            String uuid = (String) request.getParameter("uuid");
            String access_token = (String) request.getParameter("access_token");

            int user_id = tokenMapper.selectUserIdByToken(token);
            User user = userMapper.selectUserByUserId(user_id);

            if (token == null || token.equals("")) {
                return APIUtil.errorResponse("token无效");
            }
            if (uuid == null || uuid.equals("")) {
                return APIUtil.errorResponse("uuid无效");
            }
            if (access_token == null || access_token.equals("")) {
                return APIUtil.errorResponse("access_token无效");
            }

            String pay_url = REQUEST_URL + "/" + uuid + "/js_pay?open_id=" + user.getOpen_id();
            HttpPost post = new HttpPost(pay_url);
            post.addHeader("PushEnvelope-Device-Token", access_token);
            String res = AIPPost.getPost("", post);
            jsonObject = JSONObject.fromObject(res);
        } catch (Exception e) {
            status = APIStatus.ORDER_PAY_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 寄件人填写 订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        Order order = new Order(orderParam);
        try {
            //生成order的编号
            String order_number = APIRandomUtil.getRandomString();
            order.setOrder_number(order_number);
            //存储订单信息
            orderMapper.addOrder(order);
            //构建订单快递orderExpress信息
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
            for (int i = orderParam.getPackage_count(); i > 0; i--) {
                orderExpress.setOrder_number(order_number);
                //存储订单快递信息
                orderExpressMapper.addOrderExpress(orderExpress);
            }
            //进行寄件人地址的存储
            //import com.sftc.web.model.Address;
            com.sftc.web.model.Address senderAddress = new com.sftc.web.model.Address(orderParam);
            addressMapper.addAddress(senderAddress);
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, order.getId());
    }

    /**
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(Map rowData) {
        String orderExpressStr = rowData.toString();
        OrderExpress orderExpress = new Gson().fromJson(orderExpressStr, OrderExpress.class);
        APIStatus status = APIStatus.SUCCESS;
        try {
            //判断 订单是否下单 的逻辑
            Order order = orderMapper.selectOrderDetailByOrderId(orderExpress.getOrder_id());
            if (order.getRegion_type() != null && !"".equals(order.getRegion_type()) && order.getRegion_type().length() != 0) {
                APIStatus.ORDER_PACKAGE_COUNT_PULL.setMessage("订单已经下单，现在您无法再填写信息");
                status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
                return APIUtil.getResponse(status, orderExpress.getOrder_id());
            }
            //判断 同一个用户重复填写 的逻辑
            //定义preList为预获取的快递信息，realList为未填写的快递信息
            LinkedList<OrderExpress> realList = new LinkedList<OrderExpress>();
            List<OrderExpress> preList = orderExpressMapper.findAllOrderExpressByOrderId(orderExpress.getOrder_id());
            //循环遍历 prelist ，如果有重复的名字或者没有空包裹 都跳出返回问题
            //不能直接删除，删完了会影响list的数据结构。传入一个新的list
            for (OrderExpress oe : preList) {
                //某个用户只要填写过一条信息，则会跳出逻辑
                if (oe.getShip_user_id() == orderExpress.getShip_user_id()) {
                    APIStatus.ORDER_PACKAGE_COUNT_PULL.setMessage("您已经填写过信息，请勿重复填写");
                    status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
                    return APIUtil.getResponse(status, orderExpress.getOrder_id());
                }
                if (oe.getShip_user_id() == 0) {
                    //默认为0，等于0的时候加到 realList
                    realList.add(oe);
                }
            }
            //  还欠两个逻辑：1.同一个用户重复强包裹，要返回【不能重复抢】 2.还没抢完，寄件人已经下单，其他用户继续抢包裹，要返回【已经下单不能继续抢】
            if (realList.isEmpty()) {
                //list为空则返回已经抢完的信息
                APIStatus.ORDER_PACKAGE_COUNT_PULL.setMessage("包裹已经分发完");
                status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
                return APIUtil.getResponse(status, null);
            } else {
                //获取随机下标
                int random = new Random().nextInt(realList.size());
                //更新订单信息，主要是好友地址信息
                orderExpress.setState("ALREADY_FILL");
                //获取id
                orderExpress.setId(realList.get(random).getId());
                //填写包裹获取时间
                orderExpress.setReceive_time(Long.toString(System.currentTimeMillis()));
                orderExpressMapper.updateOrderExpressByOrderExpressId(orderExpress);
                //todo 添加 receive_address通知信息 此时应该是寄件人收到通知
                List<Message> messageList = messageMapper.selectMessageReceiveAddress(order.getSender_user_id());
                if (messageList.isEmpty()){
                    Message message = new Message("RECEIVE_ADDRESS",0, orderExpress.getId(),order.getSender_user_id());
                    messageMapper.insertMessage(message);
                }else {
                    Message message = messageList.get(0);
                    message.setIs_read(0);
                    message.setExpress_id(orderExpress.getId());
                    message.setUser_id(order.getSender_user_id());
                    message.setCreate_time(Long.toString(System.currentTimeMillis()));
                    messageMapper.updateMessageReceiveAddress(message);
                }
            }
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderExpress.getOrder_id());
    }

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String order_number = (String) request.getParameter("order_number");
        String order_id = (String) request.getParameter("order_id");

        Order order;
        if (order_number != null && order_number.length() != 0) {
            order = orderMapper.selectOrderDetailByOrderNumber(order_number);
        } else if (order_id != null && order_id.length() != 0) {
            order = orderMapper.selectOrderDetailByOrderId(Integer.parseInt(order_id));
        } else {
            APIStatus failStatus = APIStatus.SELECT_FAIL;
            failStatus.setMessage("order_number 或 order_id 必须传一个");
            return APIUtil.getResponse(APIStatus.SELECT_FAIL, null);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (order != null) {
            User sender = userMapper.selectUserByUserId(order.getSender_user_id());

            for (OrderExpress oe : order.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
                if (receiver != null && receiver.getAvatar() != null) {
                    oe.setShip_avatar(receiver.getAvatar());
                }
            }

            // order
            GsonBuilder gb = new GsonBuilder();
            Gson g = gb.create();
            String resultJson = new Gson().toJson(order);
            Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
            }.getType());
            orderMap.put("sender_avatar", sender.getAvatar());

            // giftCard
            GiftCard giftCard = giftCardMapper.selectGiftCardById(order.getGift_card_id());

            resultMap.put("order", orderMap);
            resultMap.put("giftCard", giftCard);
        }

        return APIUtil.getResponse(status, resultMap);


//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject1 = new JSONObject();
//        JSONObject jsonObject3 = new JSONObject();
//        Map map2 = new HashMap();
//        List list = new ArrayList();
//        try {
//            String uuid = orderExpressMapper.getUuidByOrderId(orderExpress.getOrder_id());
//            if (order.getOrder_type().equals("ORDER_FRIEND")) {
//
//                Order order1 = orderMapper.orderAndOrderExpressAndGiftDetile(orderExpress.getOrder_id());
//                jsonObject = JSONObject.fromObject(order1);
//            }
//
//            if (order.getOrder_type().equals("ORDER_MYSTERY_NATION")){
//                Order order1 = orderMapper.orderAndOrderExpressAndGiftDetile(orderExpress.getOrder_id());
//
//                jsonObject = JSONObject.fromObject(order1);
//                ORDERROUTE_URL = ORDERROUTE_URL+orderExpress.getUuid()+"&sort="+sort;
//                HttpGet get = new HttpGet(ORDERROUTE_URL);
//                String access_token = APIGetToken.getToken();
//                get.addHeader("Authorization", "bearer " + access_token);
//                String res =APIGet.getGet(get);
//
//
//                if(res.equals("[]")){
//                    Map map = new HashMap();
//                    map.put("city","深圳市");
//                    map.put("note","顺丰速运 已收取快件");
//                    map.put("creation_time","2017-6-7 15:11:57");
//                    list.add(map);
//                    String str = (String)JSONObject.fromObject(list.get(list.size()-1)).get("note");
//                    Map map1 = new HashMap();
//                    if(str.contains("已收取")){
//                        map1.put("state","已签收");
//                        jsonObject3.put("state","已签收");
//                    }
//                    jsonObject3.put("list",list);
//                    map2.put("expressInfo",jsonObject3);
//
//                }if(jsonObject1.get("Message_Type")!=null) {
//                    if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
//                        status = APIStatus.ORDERROUT_FALT;
//                    }else{
//                        jsonObject1 = JSONObject.fromObject(res);
//                        jsonObject.put(" ",jsonObject1);
//                    }
//                }
//                ORDERROUTE_URL="http://api-c-test.sf-rush.com/api/sforderservice/OrderRouteQuery?orderid=";
//            } else {
//                REQUESTS_URL = REQUESTS_URL + uuid;
//                System.out.println(uuid);
//                HttpGet get = new HttpGet(REQUESTS_URL);
//                get.addHeader("PushEnvelope-Device-Token", token.getAccess_token());
//                String res = APIGet.getGet(get);
//                jsonObject = JSONObject.fromObject(res);
//                REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
//            }
//        } catch (Exception e) {
//            System.out.println("cc");
//            status = APIStatus.PARAMETER_FAIL;
//            System.out.println(e.fillInStackTrace());
//        }
//        return APIUtil.getResponse(status, map2);
    }

    /**
     * 快递详情接口
     */
    public APIResponse sfOrderDetail(int order_id, String access_token, String uuid) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            if (uuid == null) {
                uuid = orderExpressMapper.getUuidByOrderId(order_id);
            }
            jsonObject = APISfDetail.sfDetail(uuid, access_token);
        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 修改订单接口
     */
    public APIResponse updateOrder(APIRequest request, Order order, OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;
        orderMapper.updateOrder(order);
        orderMapper.updateOrderExpress(orderExpress);
        return APIUtil.getResponse(status, null);
    }

    /**
     * 返回未被填写的包裹
     */
    public APIResponse getEmptyPackage(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String order_number = (String) request.getParameter("order_number");
        OrderExpress orderExpress = null;
        try {
            List<OrderExpress> orderExpressList = orderExpressMapper.findEmptyPackage(order_number);
            int randomOrderExpress = (int) (Math.random() * orderExpressList.size());
            orderExpress = orderExpressList.get(randomOrderExpress);
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderExpress);
    }

    /**
     * 我的订单列表
     */
    public APIResponse getMyOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();
        // verify params
        APIStatus failStatus = APIStatus.SELECT_FAIL;
        if (myOrderParam.getToken().length() == 0) {
            failStatus.setMessage("token不能为空");
            return APIUtil.getResponse(failStatus, null);
        } else if (myOrderParam.getId() == 0) {
            failStatus.setMessage("用户id不能为空");
            return APIUtil.getResponse(failStatus, null);
        } else if (myOrderParam.getPageNum() < 1 || myOrderParam.getPageSize() < 1) {
            failStatus.setMessage("分页参数无效");
            return APIUtil.getResponse(failStatus, null);
        }

        APIStatus status = APIStatus.SUCCESS;

        // handle SF orders url
        String ordersURL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        StringBuilder uuidSB = new StringBuilder();
        for (int i = 0; i < orderExpressList.size(); i++) {
            OrderExpress oe = orderExpressList.get(i);
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                uuidSB.append(oe.getUuid());
                uuidSB.append(",");
            }
        }
        String uuids = uuidSB.toString();

        // no data, return
         if (uuids.length() == 0)
            return APIUtil.getResponse(status, null);

        ordersURL = ordersURL.replace("uuid", uuids.substring(0, uuids.length() - 1));
        List<Orders> orderses;
        try { // post
            orderses = APIResolve.getOrdersJson(ordersURL, myOrderParam.getToken());
        } catch (Exception e) {
            APIStatus.SELECT_FAIL.setMessage("查询失败");
            return APIUtil.getResponse(APIStatus.SELECT_FAIL, e.getLocalizedMessage());
        }

        // Update Dankal express info
        for (Orders orders : orderses) {
            String uuid = orders.getUuid();
            String order_status = orders.getStatus();
            orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
        }

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

        return APIUtil.getResponse(status, orderCallbacks);
    }

    public APIResponse getMyFriendCircleOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();
        // verify params
        APIStatus failStatus = APIStatus.SELECT_FAIL;
        if (myOrderParam.getToken().length() == 0) {
            failStatus.setMessage("token不能为空");
            return APIUtil.getResponse(failStatus, null);
        } else if (myOrderParam.getId() == 0) {
            failStatus.setMessage("用户id不能为空");
            return APIUtil.getResponse(failStatus, null);
        } else if (myOrderParam.getPageNum() < 1 || myOrderParam.getPageSize() < 1) {
            failStatus.setMessage("分页参数无效");
            return APIUtil.getResponse(failStatus, null);
        }

        APIStatus status = APIStatus.SUCCESS;

        // handle SF orders url
        String ordersURL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        StringBuilder uuidSB = new StringBuilder();
        for (int i = 0; i < orderExpressList.size(); i++) {
            OrderExpress oe = orderExpressList.get(i);
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                uuidSB.append(oe.getUuid());
                uuidSB.append(",");
            }
        }
        String uuids = uuidSB.toString();

        // no data, return
        if (uuids.length() == 0)
            return APIUtil.getResponse(status, null);

        ordersURL = ordersURL.replace("uuid", uuids.substring(0, uuids.length() - 1));
        List<Orders> orderses;
        try { // post
            orderses = APIResolve.getOrdersJson(ordersURL, myOrderParam.getToken());
        } catch (Exception e) {
            APIStatus.SELECT_FAIL.setMessage("查询失败");
            return APIUtil.getResponse(APIStatus.SELECT_FAIL, e.getLocalizedMessage());
        }

        // Update Dankal express info
        for (Orders orders : orderses) {
            String uuid = orders.getUuid();
            String order_status = orders.getStatus();
            orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
        }

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

        return APIUtil.getResponse(status, orderCallbacks);
    }

    /**
     * 好友下单
     */
    public APIResponse friendPlace(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            HttpPost post = new HttpPost(REQUEST_URL);

            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);

            OrderExpress orderExpress = new OrderExpress((String) jsonObject1.getJSONObject("request").getJSONObject("merchant").get("uuid"), Integer.parseInt((String) jsonObject1.getJSONObject("request").get("order_id")),
                    (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"), (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"), (String) jsonObject.getJSONObject("request").get("status"));
            if (jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time") != null) {
                orderExpress.setReserve_time((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time"));
            }

            orderExpressMapper.updatePlace(orderExpress);
            Order order = new Order((Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"), (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"), Integer.parseInt((String) jsonObject1.getJSONObject("request").get("order_id")));
            orderMapper.updatePlace(order);
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }

        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 快递详情
     */
    public APIResponse selectExpressDetail(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;

        String uuid = (String) request.getParameter("uuid");
        if (uuid == null || uuid.equals("")) {
            return APIUtil.errorResponse("uuid不能为空");
        }

        JSONObject respObject = new JSONObject();

        // order
        Order order = orderMapper.placeOrderDetail(uuid);
        String regionType = order.getRegion_type();
        if (regionType.equals("REGION_NATION")) { // 大网

            // sort
            String sort = (String) request.getParameter("sort");
            sort = sort == null ? "desc" : sort;
            // GET
            String url = ORDERROUTE_URL + uuid + "&sort=" + sort;
            HttpGet get = new HttpGet(url);
            String access_token = APIGetToken.getToken();
            get.addHeader("Authorization", "bearer " + access_token);
            String res = APIGet.getGet(get);

            if (!res.equals("[]")) {
                JSONObject tempObject = JSONObject.fromObject(res);
                if (tempObject.get("Message_Type") != null) {
                    if (tempObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                        status = APIStatus.ORDERROUT_FALT;
                    } else {
                        respObject.put("sf", tempObject);
                    }
                }
            } else {
                respObject.put("sf", new JSONObject());
                status = APIStatus.ORDERROUT_FALT;
            }

        } else if (regionType.equals("REGION_SAME")) { // 同城

            // 同城订单需要access_token
            String access_token = (String) request.getParameter("access_token");
            if (access_token == null || access_token.equals("")) {
                return APIUtil.errorResponse("access_token不能为空");
            }

            try {
                respObject = APISfDetail.sfDetail(uuid, access_token);
            } catch (Exception e) {
                status = APIStatus.SELECT_FAIL;
                e.printStackTrace();
            }
        }

        respObject.put("order", order);

        return APIUtil.getResponse(status, respObject);
    }

    /**
     * 未下单详情接口
     */
    public APIResponse noPlaceOrderDetail(int order_id) {
        APIStatus status = APIStatus.SUCCESS;
        Order order = null;
        try {
            order = orderMapper.orderAndOrderExpressAndGiftDetile(order_id);
            if (order == null) {
                status = APIStatus.COURIER_NOT_FOUND;
            }
        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
        }

        return APIUtil.getResponse(status, order);
    }

    /**
     * 订单评价
     */
    public APIResponse evaluate(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        JSONObject request = jsonObject1.getJSONObject("request");
        JSONObject attributes = jsonObject1.getJSONObject("request").getJSONObject("attributes");
        try {
            if (request.get("order_type").equals("ORDER_BASIS_SAME") || request.get("order_type").equals("ORDER_MYSTERY_SAME")) {
                String pay_url = REQUEST_URL + "/" + request.get("uuid") + "/attributes/merchant_comment";
                HttpPut put = new HttpPut(pay_url);
                put.addHeader("PushEnvelope-Device-Token", (String) request.get("access_token"));
                String res = AIPPost.getPost(str, put);
                jsonObject = JSONObject.fromObject(res);

            } else {

            }
            if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
                status = APIStatus.EVALUATE_FALT;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.EVALUATE_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 取消订单
     * 测试方法在 com.sftc.web.service.OrderServiceTest
     */
    public APIResponse deleteOrder(Object object) {
//   todo:重写逻辑  多个快递信息 都要删除
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            //获取订单id，便于后续取消订单操作的取用
            int id = jsonObject1.getInt("order_id");
            jsonObject1.remove("order_id");
            //对重复取消订单的情况进行处理
            Order order = orderMapper.selectOrderDetailByOrderId(id);
            if ("Cancelled".equals(order.getIs_cancel()) || !"".equals(order.getIs_cancel())) {//is_cancle字段默认是空字符串
                APIStatus.CANCEL_ORDER_FALT.setMessage("通用：订单已经取消，请勿重复取消操作。");
                status = APIStatus.CANCEL_ORDER_FALT;
                return APIUtil.getResponse(status, "订单已经被取消");
            }
            List<OrderExpress> arrayList = orderExpressMapper.findAllOrderExpressByOrderId(id);
            //获取 拼接后的uuid串
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
                //System.out.println("-   -这是拼接前的stringBuilder"+stringBuilder);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                //System.out.println("-   -这是拼接后的stringBuilder"+stringBuilder);
                //下面是 顺丰方面取消订单的逻辑
                String str = gson.toJson(object);
                String myUrl = REQUEST_URL + "/" + stringBuilder.toString() + "/events";
                System.out.println("-   -这是myurl" + myUrl);
                HttpPost post = new HttpPost(myUrl);
                post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("event").get("access_token"));
                //向顺丰发送请求 并获取结果
                String res = AIPPost.getPost(str, post);
                jsonObject = JSONObject.fromObject(res);
                if (jsonObject.get("error") != null) {
                    APIStatus.CANCEL_ORDER_FALT.setMessage("顺丰方面的订单取消失败，系统异常，请反馈");
                    status = APIStatus.CANCEL_ORDER_FALT;
                }
                //下面是 操作order表的操作
                if (jsonObject.get("error") == null) {
                    orderMapper.updateCancelOrderById(id);
                }
            } else {//订单还未提交给顺丰的情况，只更新order的信息即可
                orderMapper.updateCancelOrderById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            APIStatus.CANCEL_ORDER_FALT.setMessage("通用：订单取消失败，系统异常，请反馈");
            status = APIStatus.CANCEL_ORDER_FALT;
        }

        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 预约时间规则
     */
    public APIResponse timeConstants(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            String constantsUrl = CONSTANTS_URL + request.getParameter("constants") + "?latitude=" + request.getParameter("latitude") + "&longitude=" + request.getParameter("longitude");
            HttpGet get = new HttpGet(constantsUrl);
            get.addHeader("PushEnvelope-Device-Token", (String) request.getParameter("access_token"));
            String res = APIGet.getGet(get);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
                status = APIStatus.CONSTANT_FALT;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.CONSTANT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 大网计价
     */
    public APIResponse OrderFreightQuery(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        JSONObject jsonObject1 = null;
        try {
            HttpPost post = new HttpPost(COUNT_PRICE);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res = AIPPost.getPost(str, post);
            jsonObject1 = JSONObject.fromObject(res);
            if (jsonObject1.get("Message_Type") != null) {
                if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.QUOTE_FAIL;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.QUOTE_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject1);
    }

    /**
     * 大网路由
     */
    public APIResponse OrderRouteQuery(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject1 = null;
        String routeUrl = ORDERROUTE_URL + request.getParameter("orderid") + "&sort=" + request.getParameter("sort");
        HttpGet get = new HttpGet(routeUrl);
        String access_token = APIGetToken.getToken();
        get.addHeader("Authorization", "bearer " + access_token);
        String res = APIGet.getGet(get);
        if (!res.equals("[]")) {
            jsonObject1 = JSONObject.fromObject(res);
            if (jsonObject1.get("Message_Type") != null) {
                if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            }
        } else {
            status = APIStatus.ORDERROUT_FALT;
        }

        return APIUtil.getResponse(status, jsonObject1);
    }

    /**
     * 大网好友下单
     */
    public APIResponse globalFriendPlace(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String orderid = APIRandomUtil.getRandomString();
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        jsonObject1.getJSONObject("sf").put("orderid", orderid);
        String str = gson.toJson(jsonObject1.getJSONObject("sf"));
        try {
            HttpPost post = new HttpPost(CREATEORDER_URL);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("Message_Type") != null) {
                if (jsonObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            } else {
                OrderExpress orderExpress = new OrderExpress();
                orderExpress.setOrder_id(Integer.parseInt((String) jsonObject1.getJSONObject("order").get("order_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.ORDERROUT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 可支付列表
     */
    public APIResponse remindPlace(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order();
        order.setSender_user_id(Integer.parseInt((String) request.getParameter("sender_user_id")));
        List<Order> orderList = orderMapper.getOrderAndExpress(order);
        return APIUtil.getResponse(status, orderList);
    }
}

