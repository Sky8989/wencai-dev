package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.*;
import com.sftc.web.model.sfmodel.Address;
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

    private static String QUOTES_URL = "http://api-dev.sf-rush.com/quotes";
    private static String REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
    private static String REQUEST_URL = "http://api-dev.sf-rush.com/requests";
    private static String PAY_URL = "http://api-dev.sf-rush.com/requests/";
    private static String CONSTANTS_URL = "http://api-dev.sf-rush.com/";
    private static String CREATEORDER_URL = "http://api-c-test.sf-rush.com/api/sforderservice/ordercreate";
    private static String COUNT_PRICE = "http://api-c-test.sf-rush.com/api/sforderservice/OrderFreightQuery";
    private static String ORDERROUTE_URL = "http://api-c-test.sf-rush.com/api/sforderservice/OrderRouteQuery?orderid=";

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

        String regionType = (String) JSONObject.fromObject(requestBody).getJSONObject("order").get("region_type");
        if (regionType.equals("REGION_SAME")) { // 同城
            return normalSameOrderCommit(requestBody);
        } else { // 大网
            return normalNationOrderCommit(requestBody);
        }
    }

    /**
     * 好友订单提交
     */
    public APIResponse friendOrderCommit(APIRequest request) {
        Object paramsBody = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(paramsBody);
        if (requestObject.containsKey("request")) { // 同城
            return friendSameOrderCommit(requestObject);
        } else { // 大网
            return null;
        }
    }

    // 好友同城订单提交
    private APIResponse friendSameOrderCommit(JSONObject requestObject) {
        APIStatus status = APIStatus.SUCCESS;
        int order_id = Integer.parseInt((String)requestObject.getJSONObject("order").get("order_id"));
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
            String paramStr = gson.toJson(JSONObject.fromObject(requestObject));
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
            String resultStr = AIPPost.getPost(paramStr, post);
            JSONObject jsonObject = JSONObject.fromObject(resultStr);

            if (jsonObject.get("errors") == null || jsonObject.get("error") == null) {
                if (requestObject.getJSONObject("order").containsKey("reserve_time")) {
                    String uuid = (String) jsonObject.getJSONObject("request").get("uuid");
                    String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
                    orderMapper.updateOrderUuidById(order_id, uuid); // 订单表更新uuid
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(order_id, uuid, reserve_time); // 快递表更新uuid和预约时间
                } else {
                    return APIUtil.errorResponse("预约时间不能为空");
                }
            } else {
                status = APIStatus.SUBMIT_FAIL;
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

        String regionType = (String) jsonObject.getJSONObject("order").get("region_type");
        if (regionType == null || regionType.length() == 0) {
            return "参数region_type不能为空";
        } else if (!regionType.equals("REGION_SAME") && !regionType.equals("REGION_NATION")) {
            return "请填写正确的region_type参数";
        }

        return null;
    }

    /**
     * 普通同城订单
     */
    private APIResponse normalSameOrderCommit(Object object) {

        Long long_order_number = (long) (Math.random() * 100000 * 1000000);
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1);
        try {
            Order order = new Order(
                    time,
                    long_order_number,
                    (String) jsonObject1.getJSONObject("request").get("pay_type"),
                    (String) jsonObject1.getJSONObject("request").get("product_type"),
                    0.0,
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),
                    (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),
                    (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                    "ORDER_BASIS",
                    Integer.parseInt((String) jsonObject1.getJSONObject("order").get("sender_user_id")));
            order.setImage((String) jsonObject1.getJSONObject("order").get("image"));
            order.setVoice((String) jsonObject1.getJSONObject("order").get("voice"));
            order.setWord_message((String) jsonObject1.getJSONObject("order").get("word_message"));
            order.setGift_card_id(Integer.parseInt((String) jsonObject1.getJSONObject("order").get("gift_card_id")));
            order.setVoice_time(Integer.parseInt((String) jsonObject1.getJSONObject("order").get("voice_time")));
            order.setRegion_type("REGION_SAME");

            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("errors") == null || jsonObject.get("error") == null) {
                if (jsonObject1.getJSONObject("order").get("reserve_time") != null) {
                    orderMapper.addOrder(order);
                    OrderExpress orderExpress = new OrderExpress(
                            time,
                            long_order_number,
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("receiver"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("mobile"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("province"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("city"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("region"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("street"),
                            (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("weight"),
                            (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("type"),
                            "",
                            Integer.parseInt((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("sender_user_id")),
                            order.getId(),
                            (String) jsonObject.getJSONObject("request").get("uuid"),
                            (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                            (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"));
                    if (jsonObject1.getJSONObject("order").get("reserve_time") != null) {
                        orderExpress.setReserve_time((String) jsonObject1.getJSONObject("order").get("reserve_time"));
                    }
                    orderExpressMapper.addOrderExpress(orderExpress);
                }
                jsonObject.put("order_id", order.getId());
            } else {
                status = APIStatus.SUBMIT_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 普通大网下单
     */
    private APIResponse normalNationOrderCommit(Object object) {
        Long long_order_number = (long) (Math.random() * 100000 * 1000000);
        String orderid = APIRandomOrderId.getRandomString(9);
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        jsonObject1.getJSONObject("sf").put("orderid", orderid);
        String str = gson.toJson(jsonObject1.getJSONObject("sf"));
        try {
            Order order = new Order(
                    time,
                    long_order_number,
                    (String) jsonObject1.getJSONObject("sf").get("pay_method"),
                    "",
                    0.0,
                    (String) jsonObject1.getJSONObject("sf").get("j_contact"),
                    (String) jsonObject1.getJSONObject("sf").get("j_tel"),
                    (String) jsonObject1.getJSONObject("sf").get("j_province"),
                    (String) jsonObject1.getJSONObject("sf").get("j_city"),
                    (String) jsonObject1.getJSONObject("sf").get("j_county"),
                    (String) jsonObject1.getJSONObject("sf").get("j_address"),
                    0.00,
                    0.00,
                    "ORDER_BASIS",
                    Integer.parseInt((String) jsonObject1.getJSONObject("order").get("sender_user_id"))
            );
            order.setImage((String) jsonObject1.getJSONObject("order").get("image"));
            order.setVoice((String) jsonObject1.getJSONObject("order").get("voice"));
            order.setWord_message((String) jsonObject1.getJSONObject("order").get("word_message"));
            order.setGift_card_id(Integer.parseInt((String) jsonObject1.getJSONObject("order").get("gift_card_id")));
            order.setVoice_time(Integer.parseInt((String) jsonObject1.getJSONObject("order").get("voice_time")));
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
                        long_order_number,
                        (String) jsonObject1.getJSONObject("sf").get("d_contact"),
                        (String) jsonObject1.getJSONObject("sf").get("d_tel"),
                        (String) jsonObject1.getJSONObject("sf").get("d_province"),
                        (String) jsonObject1.getJSONObject("sf").get("d_city"),
                        (String) jsonObject1.getJSONObject("sf").get("d_county"),
                        (String) jsonObject1.getJSONObject("sf").get("d_address"),
                        "",
                        "",
                        "待支付",
                        Integer.parseInt((String) jsonObject1.getJSONObject("order").get("sender_user_id")),
                        order.getId(),
                        orderid,
                        0.00,
                        0.00
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
        Token token = tokenMapper.getTokenByMobile("18124033797");
        HttpPost post = new HttpPost(QUOTES_URL);
        if (jsonObject.getJSONObject("request").getJSONObject("merchant").get("uuid").equals("") && jsonObject.getJSONObject("request").getJSONObject("token").get("access_token").equals("")) {
            jsonObject.getJSONObject("request").getJSONObject("merchant").put("uuid", "2c9a85895c2f618a015c2fe994ff0094");
            post.addHeader("PushEnvelope-Device-Token", token.getAccess_token());
        } else {
            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject.getJSONObject("request").getJSONObject("token").get("access_token"));
        }
        String str2 = gson1.toJson(jsonObject);
        System.out.println(jsonObject.getJSONObject("request").getJSONObject("merchant").get("uuid"));
        String res = AIPPost.getPost(str2, post);
        JSONObject jsonObject1 = JSONObject.fromObject(res);
        if (jsonObject1.get("errors") != null || jsonObject1.get("error") != null) {
            status = APIStatus.QUOTE_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject1);
    }

    /**
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            PAY_URL = PAY_URL + request.getParameter("uuid") + "/js_pay?open_id=" + request.getParameter("open_id");
            HttpPost post = new HttpPost(PAY_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) request.getParameter("access_token"));
            String res = AIPPost.getPost("", post);
            jsonObject = JSONObject.fromObject(res);
        } catch (Exception e) {
            status = APIStatus.ORDER_PAY_FAIL;
        }
        PAY_URL = "http://api-dev.sf-rush.com/requests/";
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
            long long_order_number = (long) (Math.random() * 100000 * 1000000);
            order.setOrder_number(long_order_number);
            //存储订单信息
            orderMapper.addOrder(order);
            //构建订单快递orderExpress信息
            OrderExpress orderExpress = new OrderExpress();
            orderExpress.setOrder_number(order.getOrder_number());
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
                //存储订单快递信息
                orderExpressMapper.addOrderExpress(orderExpress);
            }
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
            List<OrderExpress> list = orderExpressMapper.
                    UnWritenOrderExpressListByOrderIdAndShipnameNull(orderExpress.getOrder_id());
            // TODO: 还欠两个逻辑：1.同一个用户重复强包裹，要返回【不能重复抢】 2.还没抢完，寄件人已经下单，其他用户继续抢包裹，要返回【已经下单不能继续抢】
            if (list.isEmpty()) {
                //list为空则返回已经抢完的信息
                status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
                return APIUtil.getResponse(status, null);
            } else {
                //获取随机下标
                int random = new Random().nextInt(list.size());
                //更新订单信息，主要是好友地址信息
                orderExpress.setState("ALREADY_FILL");
                //获取id
                orderExpress.setId(list.get(random).getId());
                orderExpressMapper.updateOrderExpressByOrderExpressId(orderExpress);
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
//                System.out.println("aa");
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
     * 普通订单详情接口
     */
    public APIResponse placeOrderDetail(String uuid, String access_token) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;

        try {
            Order order = orderMapper.placeOrderDetile(uuid);
            jsonObject = APISfDetail.sfDetail(uuid, access_token);
            jsonObject.put("order", order);
        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, jsonObject);
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
                PAY_URL = PAY_URL + (String) request.get("uuid") + "/attributes/merchant_comment";
                HttpPut put = new HttpPut(PAY_URL);
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
        PAY_URL = "http://api-dev.sf-rush.com/requests/";
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 取消订单
     */
    public APIResponse deleteOrder(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            String str = gson.toJson(object);
            REQUESTS_URL = REQUESTS_URL + (String) jsonObject1.getJSONObject("event").get("uuid") + "/events";
            HttpPost post = new HttpPost(REQUESTS_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("event").get("access_token"));
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);

            if (jsonObject.get("error") == null) {
                orderMapper.deleOrderAndOrderExpress((String) jsonObject1.get("uuid"));
            }
        } catch (Exception e) {
            status = APIStatus.CANCEL_ORDER_FALT;
        }

        REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 预约时间规则
     */
    public APIResponse timeConstants(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            CONSTANTS_URL = CONSTANTS_URL + "constants/" + request.getParameter("constants") + "?latitude=" + request.getParameter("latitude") + "&longitude=" + request.getParameter("longitude");
            HttpGet get = new HttpGet(CONSTANTS_URL);
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
        CONSTANTS_URL = "http://api-dev.sf-rush.com/";
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
        try {
            ORDERROUTE_URL = ORDERROUTE_URL + request.getParameter("orderid") + "&sort=" + request.getParameter("sort");
            HttpGet get = new HttpGet(ORDERROUTE_URL);
            String access_token = APIGetToken.getToken();
            get.addHeader("Authorization", "bearer " + access_token);
            String res = APIGet.getGet(get);
            if (!res.equals("[]")) {
                jsonObject1 = JSONObject.fromObject(res);
            } else {
                status = APIStatus.ORDERROUT_FALT;
            }

            if (jsonObject1.get("Message_Type") != null) {
                if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            }
            ORDERROUTE_URL = "http://api-c-test.sf-rush.com/api/sforderservice/OrderRouteQuery?orderid=";
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.ORDERROUT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject1);
    }

    /**
     * 大网好友下单
     */
    public APIResponse globalFriendPlace(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String orderid = APIRandomOrderId.getRandomString(9);
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

