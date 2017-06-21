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
import com.sftc.web.model.sfmodel.Orders;
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
    Gson gson = new Gson();

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

    /*
     * 普通提交订单 普通同城订单
     */
    public APIResponse placeOrder(Object object) {

        Long long_order_number = (long) (Math.random() * 100000 * 1000000);
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1);
        System.out.println(str);
        try {
            Order order = new Order(time, long_order_number,
                    "待支付",
                    time,
                    (String) jsonObject1.getJSONObject("request").get("pay_type"),
                    (String) jsonObject1.getJSONObject("request").get("product_type"), 0.0,
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),
                    (String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),
                    (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),
                    (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                    "ORDER_BASIS_SAME",
                    Integer.parseInt((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("sender_user_id")));
            System.out.println("aa");
            ValidateNull.validateParamT(jsonObject1, order);
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject.get("errors") == null || jsonObject.get("error") == null) {
                if ((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time") != null) {
                    orderMapper.addOrder(order);
                    OrderExpress orderExpress = new OrderExpress(time, long_order_number, (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("receiver"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("mobile"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("province"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("city"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("region"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("street"), (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("weight"),//包裹类型
                            (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("type"), "待支付", Integer.parseInt((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("sender_user_id")), order.getId(), (String) jsonObject.getJSONObject("request").get("uuid"), (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                            (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"));
                    if ((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time") != null) {
                        orderExpress.setReserve_time((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time"));
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

    /*
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

    /*
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

    /*
     * 寄件人填写 订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        //System.out.println("-   -这是orderParm"+orderParam.toString());
        //自动将sender_id order_type region_type 封装到order
        Order order = new Order(orderParam);
        //System.out.println("-   -这是order"+order.toString());
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
            orderExpress.setReserve_time("0000");
            //装入order的id，注意不是order编号，orderExpress需要order的id外键
            orderExpress.setOrder_id(order.getId());
            //System.out.println("-   -包裹数量"+orderParam.getPackage_count());
            for (int i = orderParam.getPackage_count(); i > 0 ;i--){
                //存储订单快递信息
                orderExpressMapper.addOrderExpress(orderExpress);
                //System.out.println("-   -存储快递信息的次数" + i);
            }
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, order.getId());
    }

    /*
     * @好友填写寄件订单
     *
     */
    public synchronized APIResponse friendFillOrder(Map rowData) {
        //对传进来的json参数 转换为对象和flag
        int is_same = Integer.parseInt(rowData.get("is_same").toString());
        //去掉 Map中的is_same元素 便于Gson整体封装剩余元素到OrderExpress中
        rowData.remove("is_same");
        String orderExpressStr = rowData.toString();
        OrderExpress orderExpress = new Gson().fromJson(orderExpressStr, OrderExpress.class);
        APIStatus status = APIStatus.SUCCESS;
        try{
            List<OrderExpress> list = orderExpressMapper.
                    UnWritenOrderExpressListByOrderIdAndShipnameNull(orderExpress.getOrder_id());
            if (list.isEmpty()) {
                //list为空则返回已经抢完的信息
                status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
                return APIUtil.getResponse(status, orderExpress.getOrder_id());
            } else {
                //获取随机下标
                int random = new Random().nextInt(list.size());
                //System.out.println("-   -随机数"+random+"-     -list的size"+list.size());
                //更新订单信息，主要是好友地址信息
                orderExpress.setState("ALREADY_FILL");
                //获取id
                orderExpress.setId(list.get(random).getId());
                orderExpressMapper.updateOrderExpressByOrderExpressId(orderExpress);
//                //order的订单类型更新 //拆分字段后，无须更新操作
//                Order order = new Order();
//                order.setId(orderExpress.getOrder_id());
//                if(is_same == 1){
//                    //更新order_type为 神秘同城 订单
//                    order.setOrder_type("ORDER_MYSTERY_SAME");
//                } else {
//                    //更新order_type为 神秘大网 订单
//                    order.setOrder_type("ORDER_MYSTERY_NATION");
//                }
//                orderMapper.updateOrderTypeById(order);
            }
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderExpress.getOrder_id());
    }

//    /*
//    * @查看所有订单
//    * */
//    public APIResponse getAllOrder(APIRequest request) {
//        System.out.println("11");
//        APIStatus status = APIStatus.SUCCESS;
//        String user_id_str = (String) request.getParameter("user_id");
//        String state = (String) request.getParameter("state");
//        int user_id = 0;
//        try {
//            user_id = Integer.parseInt(user_id_str);
//        } catch (NumberFormatException e) {
//
//        }
//        Order order = new Order();
//        order.setSender_user_id(user_id);
//        order.setState(state);
//        List<Order> list = orderMapper.myOrderList(order);
//
//        if (list.size() == 0) {
//            status = APIStatus.ORDER_NOT_FOUND;
//            System.out.print("11");
//        }
//        return APIUtil.getResponse(status, list);
//    }

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String order_number = (String) request.getParameter("order_number");
        String order_id = (String) request.getParameter("order_id");

        Order order;
        if (order_number.length() != 0) {
            order = orderMapper.selectOrderDetailByOrderNumber(order_number);
        } else if (order_id.length() != 0) {
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

            resultMap.put("order",orderMap);
            resultMap.put("giftCard",giftCard);
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
    /*
        * @顺丰订单详情接口
        * */

    public APIResponse sfOrderDetail(int order_id, String access_token, String uuid) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        try {
            System.out.println(uuid);
            if (uuid == null) {
                uuid = orderExpressMapper.getUuidByOrderId(order_id);
            }
            jsonObject = APISfDetail.sfDetail(uuid, access_token);

        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
            System.out.println(e.fillInStackTrace());
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /*
    * 修改订单接口
    * */
    public APIResponse updateOrder(APIRequest request, Order order, OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;

        orderMapper.updateOrder(order);
        orderMapper.updateOrderExpress(orderExpress);
        return APIUtil.getResponse(status, null);
    }

    /*
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

    /*
     * C01 我的订单
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

    /*
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
            if ((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time") != null) {
                orderExpress.setReserve_time((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time"));
            }

            orderExpressMapper.updatePlace(orderExpress);
            Order order = new Order((Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"), (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"), Integer.parseInt((String) jsonObject1.getJSONObject("request").get("order_id")));
            orderMapper.updatePlace(order);
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            System.out.println(e.fillInStackTrace());
        }

        return APIUtil.getResponse(status, jsonObject);
    }


        /*
        * @普通订单详情接口
        * */

    public APIResponse placeOrderDetail(String uuid, String access_token) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;

        try {
            Order order = orderMapper.placeOrderDetile(uuid);
            jsonObject = APISfDetail.sfDetail(uuid, access_token);
            jsonObject.put("order", order);
        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
            System.out.println(e.fillInStackTrace());
        }
        return APIUtil.getResponse(status, jsonObject);
    }
 /*
        * @未下单详情接口
        * */

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
     /*
        * @对商家评价
        * */

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
            System.out.println(e.fillInStackTrace());
            status = APIStatus.EVALUATE_FALT;
        }
        PAY_URL = "http://api-dev.sf-rush.com/requests/";
        return APIUtil.getResponse(status, jsonObject);
    }
       /*
        * @取消订单
        *
        * */

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

    /*
     * @预约时间规则
     *
     * */
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
            System.out.println(e.fillInStackTrace());
            status = APIStatus.CONSTANT_FALT;
        }
        CONSTANTS_URL = "http://api-dev.sf-rush.com/";
        return APIUtil.getResponse(status, jsonObject);
    }

    /*
     * @大网普通下单
     *
     * */
    public APIResponse createOrder(Object object) {
        //System.out.println("hxy:here comes a createOrder----------------------------");
        Long long_order_number = (long) (Math.random() * 100000 * 1000000);
        String orderid = APIRandomOrderId.getRandomString(9);
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        jsonObject1.getJSONObject("sf").put("orderid", orderid);
        String str = gson.toJson(jsonObject1.getJSONObject("sf"));
        //System.out.println("hxy:here shows a str
        //修改 order_type 为 "ORDER_BASIS_NATION" ,by hxy
        try {
            Order order = new Order(
                    time,
                    long_order_number,
                    "待支付",
                    time,
                    (String) jsonObject1.getJSONObject("sf").get("pay_method"),
                    "",
                    0.0,
                    (String) jsonObject1.getJSONObject("sf").get("j_contact"),
                    (String) jsonObject1.getJSONObject("sf").get("j_tel"),
                    (String) jsonObject1.getJSONObject("sf").get("j_province"),
                    (String) jsonObject1.getJSONObject("sf").get("j_city"),
                    (String) jsonObject1.getJSONObject("sf").get("j_county"),
                    (String) jsonObject1.getJSONObject("sf").get("j_address"),
                    0.00, 0.00,
                    "ORDER_BASIS_NATION",
                    Integer.parseInt((String) jsonObject1.getJSONObject("order").get("sender_user_id"))
            );
            ValidateNull.validateParam(jsonObject1, order);
            //发送请求给sf，获取订单结果
            HttpPost post = new HttpPost(CREATEORDER_URL);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res = AIPPost.getPost(str, post);
            //System.out.println(res);
            jsonObject = JSONObject.fromObject(res);
            if (jsonObject1.get("Message_Type") != null) {
                if (jsonObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.SUBMIT_FAIL;
                }
            } else {
                //存储 订单信息
                orderMapper.addOrder(order);
                OrderExpress orderExpress = new OrderExpress(time, long_order_number,
                        (String) jsonObject1.getJSONObject("sf").get("d_contact"),
                        (String) jsonObject1.getJSONObject("sf").get("d_tel"),
                        (String) jsonObject1.getJSONObject("sf").get("d_province"),
                        (String) jsonObject1.getJSONObject("sf").get("d_city"),
                        (String) jsonObject1.getJSONObject("sf").get("d_county"),
                        (String) jsonObject1.getJSONObject("sf").get("d_address"),
                        "",//包裹类型
                        "",
                        "待支付",
                        Integer.parseInt((String) jsonObject1.getJSONObject("order").get("sender_user_id")),
                        order.getId(), orderid, 0.00,
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

    /*
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
            System.out.println(e.fillInStackTrace());
            status = APIStatus.QUOTE_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject1);
    }

    /*
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
            System.out.println(e.fillInStackTrace());
            status = APIStatus.ORDERROUT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject1);
    }

    /*
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
            System.out.println(e.fillInStackTrace());
            status = APIStatus.ORDERROUT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /*
     * 寄件人回到首页时发起支付
     */
    public APIResponse remindPlace(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order();
        order.setState("WAIT_HAND_OVER");
        order.setSender_user_id(Integer.parseInt((String) request.getParameter("sender_user_id")));
        List<Order> orderList = orderMapper.getOrderAndExpress(order);
        return APIUtil.getResponse(status, orderList);
    }
}

