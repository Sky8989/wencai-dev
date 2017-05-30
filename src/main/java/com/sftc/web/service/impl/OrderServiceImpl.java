package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.Error;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.*;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 订单操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static String QUOTES_URL = "http://api-dev.sf-rush.com/quotes";
    private static String REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
    private static String REQUEST_URL = "http://api-dev.sf-rush.com/requests";

    Gson gson = new Gson();
    Gson gson1 = new Gson();
    String time = Long.toString(System.currentTimeMillis());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;

    /*
     * 普通提交订单
     */


    @Override
    public APIResponse placeOrder(Object object) {
        String order_number= UUID.randomUUID().toString();


        APIStatus status = APIStatus.SUCCESS;

        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1);
        System.out.println(str);
        try {
            User user = userMapper.selectUserByPhone((String) jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"));
            Order order = new Order(time,order_number, "待支付",time,(String)jsonObject1.getJSONObject("request").get("pay_type"),

                    (String)jsonObject1.getJSONObject("request").get("product_type"),0.0,  (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"), (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),(String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),(String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),(String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),(String)jsonObject1.getJSONObject("request").getJSONObject("order").get("word_message"),(String)jsonObject1.getJSONObject("request").getJSONObject("order").get("image"),(String)jsonObject1.getJSONObject("request").getJSONObject("order").get("voice"),
                    (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"), (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),Integer.parseInt((String)jsonObject1.getJSONObject("request").getJSONObject("order").get("gift_card_id")),"普通订单",user.getId());
           System.out.println("aa");
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token",(String)jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str,post);
            jsonObject = JSONObject.fromObject(res);
            if(jsonObject.get("errors")==null||jsonObject.get("error")==null) {
                orderMapper.addOrder(order);
                OrderExpress orderExpress = new OrderExpress(time,order_number,(String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("receiver"),(String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("mobile"),(String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("province"),
                        (String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("city"),(String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("region"), (String)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("street"), (String)jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("type"),
                        (String)jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("comments"),"待支付",user.getId(),order.getId(), (String)jsonObject.getJSONObject("request").get("uuid"),(Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                        (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"));
                orderExpressMapper.addOrderExpress(orderExpress);
            } else {

                status = APIStatus.SUBMIT_FAIL;
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            status = APIStatus.SUBMIT_FAIL;

        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /*
         * 计价
         */
    public APIResponse countPrice(Requests requests) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(requests);
        JSONObject jsonObject = JSONObject.fromObject(str);

        Request request = (Request)JSONObject.toBean((JSONObject)jsonObject.get("request"), Request.class);
        Token token = tokenMapper.getTokenByMobile("18124033797");

        HttpPost post = new HttpPost(QUOTES_URL);
        if(request.getMerchant().getUuid().equals("")&&requests.getRequest().getToken().getAccess_token().equals("")) {
            request.getMerchant().setUuid("2c9a85895c2f618a015c2fe994ff0094");
            post.addHeader("PushEnvelope-Device-Token",token.getAccess_token());
        }else{
            post.addHeader("PushEnvelope-Device-Token",requests.getRequest().getToken().getAccess_token());
        }
        requests.setRequest(request);
        JSONObject str1 = JSONObject.fromObject(requests);
        System.out.println(requests.getRequest().getMerchant().getUuid());
        String str2 = gson1.toJson(str1);
        String res = AIPPost.getPost(str2,post);
        JSONObject jsonObject1 = JSONObject.fromObject(res);

        if(jsonObject1.get("errors")!=null||jsonObject1.get("error")!=null){
            status = APIStatus.QUOTE_FAIL;
        }

        return APIUtil.getResponse(status,jsonObject1);

    }


    /*
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order((String) request.getParameter("order_number"),
                Long.toString(System.currentTimeMillis()), "待揽件");
        try {
            orderMapper.updateOrder(order);
        } catch (Exception e) {
            status = APIStatus.ORDER_PAY_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }


    /*
     * 寄件人填写 订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        List<OrderExpress> orderExpressList = orderParam.getOrderExpressList();
        Order order = new Order(orderParam);
        long long_order_number = (long) (Math.random() * 100000 * 1000000);
        String order_number = long_order_number + "";
        order.setOrder_number(order_number);
        order.setOrder_type("好友寄件");
        User user = userMapper.selectUserByPhone(orderParam.getSender_mobile());
        order.setSender_user_id(user.getId());
        try {
            orderMapper.addOrder(order);
            for (int i = 0; i < orderExpressList.size(); i++) {
                orderExpressList.get(i).setOrder_number(order_number);
                orderExpressList.get(i).setState("待下单");
                orderExpressList.get(i).setUuid("");
                orderExpressList.get(i).setSender_user_id(user.getId());
                OrderExpress orderExpress = new OrderExpress(
                        orderExpressList.get(i).getOrder_number(),
                        orderExpressList.get(i).getPackage_type(),
                        orderExpressList.get(i).getObject_type(),
                        order.getId(),
                        order.getCreate_time(),
                        orderExpressList.get(i).getIs_use(),
                        orderExpressList.get(i).getState(),
                        orderExpressList.get(i).getUuid(),
                        orderExpressList.get(i).getSender_user_id()
                        );
                orderExpressMapper.addOrderExpress(orderExpress);
            }
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, order.getOrder_number());
    }


    /*
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;
        //orderExpress.setUuid((String)jsonObject.getJSONObject("request").get("uuid"));
//            status = APIStatus.SUBMIT_FAIL;
     try {
         orderExpress.setUuid("");
         orderExpressMapper.updateOrderExpress(orderExpress);
     }catch(Exception e){
         status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    /*
    * @查看所有订单
    * */
    public APIResponse getAllOrder(APIRequest request) {
        System.out.println("11");
        APIStatus status = APIStatus.SUCCESS;
        String user_id_str = (String) request.getParameter("user_id");
        String state = (String) request.getParameter("state");
        int user_id = 0;
        try {
            user_id = Integer.parseInt(user_id_str);
        } catch (NumberFormatException e) {

        }
        Order order = new Order();
        order.setSender_user_id(user_id);
        order.setState(state);
        List<Order> list = orderMapper.myOrderList(order);

        if (list.size() == 0) {
            status = APIStatus.ORDER_NOT_FOUND;
            System.out.print("11");
        }
        return APIUtil.getResponse(status, list);
    }

    /*
    * @好友订单订单详情接口
    * */
    public APIResponse getOrderDetile(Order order,OrderExpress orderExpress,Token token) {
        APIStatus status = APIStatus.SUCCESS;

        JSONObject jsonObject = new JSONObject();

       try{

           String uuid = orderExpressMapper.getUuidByOrderId(orderExpress.getOrder_id());
           if(order.getOrder_type().equals("好友订单")){

               Order order1 = orderMapper.orderAndOrderExpressAndGiftDetile(orderExpress.getOrder_id());
              jsonObject =  JSONObject.fromObject(order1);
           }
           else {
               System.out.println(uuid);
              jsonObject =  APISfDetail.sfOrderDetail(token.getAccess_token(),uuid);

           }
    }
       catch (Exception e){
           status  = APIStatus.PARAMETER_FAIL;
        System.out.println( e.fillInStackTrace());
      }
        return APIUtil.getResponse(status, jsonObject);
    }
    /*
        * @顺丰订单详情接口
        * */

    public APIResponse sfOrderDetail(int order_id,String access_token,String uuid){
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject =null;
        try {
            if (uuid==null) {
                uuid = orderExpressMapper.getUuidByOrderId(order_id);
            }
            jsonObject = APISfDetail.sfOrderDetail(access_token, uuid);
        }catch (Exception e){
            status  = APIStatus.PARAMETER_FAIL;
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
    public APIResponse getMyOrderList(MyOrderParam myOrderParam) {
        APIStatus status = APIStatus.SUCCESS;
        String ORDERS_URL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        String uuids = "";
        List<OrderCallback> orderCallbacks = null;
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        for (OrderExpress oe : orderExpressList) {
            uuids = uuids + oe.getUuid() + ",";
        }
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("uuid", uuids);
        try {
            List<Orders> orderses = APIResolve.getOrdersJson(ORDERS_URL, myOrderParam.getToken());
            for (Orders orders : orderses) {
                String uuid = orders.getUuid();
                String order_status = sfStatus(orders.getStatus());
                orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
            }
            if (myOrderParam.getState().equals("")) {
                myOrderParam.setState(null);
            }
            orderCallbacks = orderExpressMapper.findMyOrderExpress
                    (new OrderExpress(myOrderParam.getId(), myOrderParam.getState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderCallbacks);
    }

    public String sfStatus(String status) {
        if (status.equals("INIT")) {
            status = "下单";
        } else if (status.equals("PAYING")) {
            status = "支付中";
        } else if (status.equals("WAIT_HAND_OVER")) {
            status = "待揽件";
        } else if (status.equals("DELIVERING")) {
            status = "派送中";
        } else if (status.equals("FINISHED")) {
            status = "已完成";
        } else if (status.equals("ABNORMAL")) {
            status = "不正常的";
        } else if (status.equals("CANCELED")) {
            status = "取消单";
        } else if (status.equals("WAIT_REFUND")) {
            status = "等待退款";
        } else if (status.equals("REFUNDING")) {
            status = "退款中";
        } else if (status.equals("REFUNDED")) {
            status = "已退款";
        }
        return status;
    }



    @Override

    public APIResponse friendPlace(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        HttpPost post = new HttpPost(REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token",(String)jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
        String res = AIPPost.getPost(str,post);
        JSONObject jsonObject = JSONObject.fromObject(res);
        OrderExpress orderExpress = new OrderExpress((String)jsonObject1.getJSONObject("request").getJSONObject("merchant").get("uuid"),(String)jsonObject1.getJSONObject("request").get("order_number"),
                (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),(Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"));
       System.out.println(orderExpress.getUuid()+(String)jsonObject.getJSONObject("request").get("order_number"));
        orderExpressMapper.updatePlace(orderExpress);
        Order order = new Order((Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),(Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),(String)jsonObject1.getJSONObject("request").get("order_number"));
        orderMapper.updatePlace(order);
        return APIUtil.getResponse(status,jsonObject );
    }


        /*
        * @普通订单详情接口
        * */

    public APIResponse placeOrderDetail(String uuid,String access_token){
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject =null;

        try {
            Order order = orderMapper.placeOrderDetile(uuid);
            System.out.println("aa");
            jsonObject = APISfDetail.sfOrderDetail(access_token,uuid);
            jsonObject.put("order",order);
            System.out.println(order);
        }catch (Exception e){
            status  = APIStatus.PARAMETER_FAIL;
            System.out.println(e.fillInStackTrace());
        }
        return APIUtil.getResponse(status, jsonObject);
    }
}

