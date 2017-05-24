package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;



import com.sftc.web.model.Error;
import com.sftc.web.model.reqeustParam.OrderParam;

import com.sftc.web.model.sfmodel.Request;

import com.sftc.web.model.reqeustParam.OrderParam;

import com.sftc.web.model.sfmodel.Requests;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.*;

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

    public APIResponse placeOrder(Object object) {
        String order_number = UUID.randomUUID().toString();
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1.get("requests"));
        try {
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token","7nWq8uExhVUoE7EW4ud2");//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str,post);
            jsonObject = JSONObject.fromObject(res);
<<<<<<< HEAD
            System.out.println(jsonObject.getJSONObject("request"));
          Request request = (Request) JSONObject.toBean((JSONObject)jsonObject.get("request"),Request.class);
            System.out.println("CC");
           Error error = (Error) JSONObject.toBean((JSONObject) jsonObject.get("error"), Error.class);
           Order order = (Order) JSONObject.toBean((JSONObject)jsonObject1.get("order"),Order.class);
            OrderExpress orderExpress = (OrderExpress) JSONObject.toBean((JSONObject)jsonObject1.get("orderExpress"),OrderExpress.class);
=======
            Error error = (Error) JSONObject.toBean((JSONObject) jsonObject.get("error"), Error.class);
            Order order = (Order) JSONObject.toBean((JSONObject)jsonObject1.get("order"),Order.class);
            OrderExpress orderExpress = (OrderExpress) JSONObject.toBean((JSONObject)jsonObject1.get("orderExpress"),OrderExpress.class);

>>>>>>> yqy
              order.setCreate_time(time);
              order.setGmt_order_create(time);
             order.setOrder_number(order_number);
             order.setState("待支付");
              orderExpress.setCreate_time(time);
              orderExpress.setOrder_number(order_number);
                orderExpress.setState("待支付");
            orderExpress.setUuid((String)jsonObject.getJSONObject("request").get("uuid"));

<<<<<<< HEAD

=======
>>>>>>> yqy
            if(error==null) {
                System.out.println("BBB");
                orderMapper.addOrder(order);
                System.out.println(order.getId());
                jsonObject.put("id", order.getId());
                orderExpressMapper.addOrderExpress(orderExpress);
            } else {
                status = APIStatus.SUBMIT_FAIL;
            }
        } catch (Exception e) {



        }
        return APIUtil.getResponse(status, jsonObject);
    }

    @Override
    public APIResponse placeOrder1(Requests requests) {
        String order_number=Long.toString(System.currentTimeMillis());
        APIStatus status = APIStatus.SUCCESS;
//        Order order  = new Order(time,order_number,"待支付", time,requests.getRequest().getPay_type(),
//                requests.getRequest().getProduct_type(), requests.getOrder().getFreight(),requests.getRequest().getSource().getAddress().getReceiver(),
//                requests.getRequest().getSource().getAddress().getMobile(),requests.getRequest().getSource().getAddress().getProvince(),
//                requests.getRequest().getSource().getAddress().getCity(),requests.getRequest().getSource().getAddress().getRegion(),requests.getRequest().getSource().getAddress().getStreet(),
//                requests.getOrder().getWord_message(), requests.getOrder().getImage(), requests.getOrder().getVoice(),
//       requests.getRequest().getSource().getCoordinate().getLongitude(), requests.getRequest().getSource().getCoordinate().getLatitude(), requests.getOrder().getSender_user_id(), requests.getOrder().getGift_card_id());
//
//        OrderExpress orderExpress = new OrderExpress(time, order_number,requests.getRequest().getTarget().getAddress().getReceiver(),
//                requests.getRequest().getTarget().getAddress().getMobile(),requests.getRequest().getTarget().getAddress().getProvince(),
//                requests.getRequest().getTarget().getAddress().getCity(),requests.getRequest().getTarget().getAddress().getRegion(),
//                requests.getRequest().getTarget().getAddress().getStreet(),requests.getRequest().getPackages().get(0).getType(), requests.getRequest().getPackages().get(0).getComments(),
//                "待支付", requests.getOrderExpress().getSender_user_id(),requests.getOrderExpress().getOrder_id(), requests.getOrderExpress().getShip_user_id());

        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(requests);
        String str = gson.toJson(jsonObject1.get("requests"));
        System.out.println(str);
        try {
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token","7nWq8uExhVUoE7EW4ud2");//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str,post);
            jsonObject = JSONObject.fromObject(res);
            Error error = (Error) JSONObject.toBean((JSONObject) jsonObject.get("error"), Error.class);
            Order order1 = (Order) JSONObject.toBean((JSONObject)jsonObject1.get("order"),Order.class);
            OrderExpress orderExpress1 = (OrderExpress) JSONObject.toBean((JSONObject)jsonObject1.get("orderExpress"),OrderExpress.class);
            if(error==null) {
                orderMapper.addOrder(order1);
                orderExpressMapper.addOrderExpress(orderExpress1);
            } else {
                status = APIStatus.SUBMIT_FAIL;
            }
        } catch (Exception e) {

            status = APIStatus.ORDER_NOT_FOUND;

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
     * 好友寄件提交订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        List<OrderExpress> orderExpressList = orderParam.getOrderExpressList();
        Order order = new Order(orderParam);
        String order_number = UUID.randomUUID().toString();
        order.setOrder_number(order_number);
        try {
            orderMapper.addOrder(order);
            for (int i = 0; i < orderExpressList.size(); i++) {
                orderExpressList.get(i).setOrder_number(order_number);
                orderExpressList.get(i).setState("待下单");
                orderExpressList.get(i).setUuid("");
                OrderExpress orderExpress = new OrderExpress(
                        orderExpressList.get(i).getOrder_number(),
                        orderExpressList.get(i).getPackage_type(),
                        orderExpressList.get(i).getObject_type(),
                        orderExpressList.get(i).getSender_user_id(),
                        orderExpressList.get(i).getShip_user_id(),
                        orderExpressList.get(i).getOrder_id(),
                        order.getCreate_time(),
                        orderExpressList.get(i).getIs_use(),
                        orderExpressList.get(i).getState(),
                        orderExpressList.get(i).getUuid()
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
    public synchronized APIResponse friendFillOrder(Object object) {
        System.out.println(object);
        APIStatus status = APIStatus.SUCCESS;
<<<<<<< HEAD
=======


>>>>>>> yqy
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1.get("requests"));
        HttpPost post = new HttpPost(REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token","7nWq8uExhVUoE7EW4ud2");//97uAK7HQmDtsw5JMOqad
        String res = AIPPost.getPost(str,post);
        jsonObject = JSONObject.fromObject(res);
        System.out.println("ss");
        OrderExpress orderExpress = (OrderExpress) JSONObject.toBean((JSONObject)jsonObject1.get("orderExpress"),OrderExpress.class);

        orderExpress.setUuid((String)jsonObject.getJSONObject("request").get("uuid"));
        if(jsonObject.get("error")!=null||jsonObject.get("errors")!=null){
            status = APIStatus.SUBMIT_FAIL;
        }else {
            System.out.println(orderExpress.getUuid());
            orderExpressMapper.updateOrderExpress(orderExpress);
        }



        return APIUtil.getResponse(status, jsonObject);
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
    * @订单详情接口
    * */
    public APIResponse getOrderDetile(Requests requests) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(requests);
//       try{
        User merchant = userMapper.getUuidAndtoken(85);
        REQUESTS_URL = REQUESTS_URL +requests.getRequest().getOrder().getJob_number();
        HttpGet post = new HttpGet(REQUESTS_URL);

        post.addHeader("PushEnvelope-Device-Token",merchant.getToken().getAccess_token());
        String res = APIGet.getPost(str,post);
       JSONObject jsonObject = (JSONObject)JSONObject.fromObject(res).get("request");
      //  requests.setJsonObject(jsonObject);
        Order order = orderMapper.orderDetile(85);
        order.setRequest(jsonObject);
       // requests.setOrder(order);
    //}


//       catch (Exception e){
//           e.fillInStackTrace();
//       }

        return APIUtil.getResponse(status, order);
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
        APIStatus status = APIStatus.SUCCESS;
        String id = request.getParameter("id").toString();
        String state = request.getParameter("state").toString();
        OrderExpress orderExpress = new OrderExpress();
        orderExpress.setId(Integer.parseInt(id));
        if (!state.equals("")) {
            orderExpress.setState(state);
        }
        List<Order> orderList = orderMapper.myOrderLists(orderExpress);
        return APIUtil.getResponse(status, orderList);
    }
}

