package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;

import com.sftc.web.mapper.*;
import com.sftc.web.model.*;


import com.sftc.web.model.Error;
import com.sftc.web.model.reqeustParam.OrderParam;

import com.sftc.web.model.sfmodel.Request;
import com.sftc.web.model.sfmodel.Requests;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public APIResponse placeOrder(Object object) {
        String order_number=Long.toString(System.currentTimeMillis());
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1.get("requests"));
        try {

            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token","7nWq8uExhVUoE7EW4ud2");//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str,post);
             jsonObject = JSONObject.fromObject(res);
           Error error = (Error) JSONObject.toBean((JSONObject) jsonObject.get("error"), Error.class);
           Order order = (Order) JSONObject.toBean((JSONObject)jsonObject1.get("order"),Order.class);
            OrderExpress orderExpress = (OrderExpress) JSONObject.toBean((JSONObject)jsonObject1.get("orderExpress"),OrderExpress.class);
              order.setCreate_time(time);
              order.setGmt_order_create(time);
             order.setOrder_number(order_number);
             order.setState("待支付");
              orderExpress.setCreate_time(time);
              orderExpress.setOrder_number(order_number);
                orderExpress.setState("待支付");
            if(error==null) {
                orderMapper.addOrder(order);
                orderExpressMapper.addOrderExpress(orderExpress);
            }else{
                status = APIStatus.ORDER_SUBMIT_FAIL;
            }
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
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
        request.getMerchant().setUuid("2c9a85895c163a16015c165321dc0045");
            requests.setRequest(request);
        JSONObject str1 = JSONObject.fromObject(requests);
        System.out.println(requests.getRequest().getMerchant().getUuid());
        String str2 = gson1.toJson(str1);
        System.out.println(str1);

        Token token = tokenMapper.getTokenByMobile("13632571782");
        HttpPost post = new HttpPost(QUOTES_URL);

        post.addHeader("PushEnvelope-Device-Token",token.getAccess_token());
        String res = AIPPost.getPost(str2,post);
        JSONObject jsonObject1 = JSONObject.fromObject(res);
        if(jsonObject1.get("error")!=null){
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
                OrderExpress orderExpress = new OrderExpress(
                        orderExpressList.get(i).getOrder_number(),
                        orderExpressList.get(i).getPackage_type(),
                        orderExpressList.get(i).getObject_type(),
                        orderExpressList.get(i).getSender_user_id(),
                        orderExpressList.get(i).getShip_user_id(),
                        orderExpressList.get(i).getOrder_id(),
                        order.getCreate_time(),
                        orderExpressList.get(i).getIs_use(),
                        orderExpressList.get(i).getState()
                        );
                orderExpressMapper.addOrderExpress(orderExpress);
            }
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, order.getOrder_number());
    }


    /*
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(APIRequest request,Object object) {
        APIStatus status = APIStatus.SUCCESS;
        OrderExpress orderExpress = new OrderExpress(request);
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(REQUEST_URL);
        System.out.println(str);
        post.addHeader("PushEnvelope-Device-Token","97uAK7HQmDtsw5JMOqad");//97uAK7HQmDtsw5JMOqad

        String res = AIPPost.getPost(str,post);
       JSONObject jsonObject = JSONObject.fromObject(res);
        if(jsonObject.get("error")!=null){
            status = APIStatus.QUOTE_FAIL;
        }
        try {
            orderExpressMapper.updateOrderExpress(orderExpress);
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
        }

        return APIUtil.getResponse(status, null);
    }

    /*
    * @查看所有订单
    * */
    public APIResponse getAllOrder(APIRequest request) {
        System.out.println("11");
        APIStatus status = APIStatus.SUCCESS;
        String user_id_str = (String)request.getParameter("user_id");
        String state = (String)request.getParameter("state");
        int user_id=0;
        try {
             user_id = Integer.parseInt(user_id_str);
        }catch (NumberFormatException e){

        }
        Order order = new Order();
        order.setSender_user_id(user_id);
        order.setState(state);
        List<Order> list = orderMapper.myOrderList(order);

        if(list.size()==0){
            status = APIStatus.ORDER_NOT_FOUND;
            System.out.print("11");
        }
        return  APIUtil.getResponse(status, list);
    }
    /*
    * @订单详情接口
    * */

    @Override
    public APIResponse getOrderDetile(Requests requests) {

        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(requests);
//       try{
       User merchant =  userMapper.getUuidAndtoken(85);
        REQUESTS_URL = REQUESTS_URL+merchant.getUuid();
        HttpGet post = new HttpGet(REQUESTS_URL);
        post.addHeader("PushEnvelope-Device-Token",merchant.getToken().getAccess_token());
        String res = APIGet.getPost(str,post);
       JSONObject jsonObject = JSONObject.fromObject(res);
      //  requests.setJsonObject(jsonObject);
        Order order = orderMapper.orderDetile(85);
       // requests.setOrder(order);
    //}
//       catch (Exception e){
//           e.fillInStackTrace();
//       }

        return  APIUtil.getResponse(status, requests);
    }
    /*
    * 修改订单接口
    * */
    public APIResponse updateOrder(APIRequest request,Order order,OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;

        orderMapper.updateOrder(order);
        orderMapper.updateOrderExpress(orderExpress);
        return  APIUtil.getResponse(status, null);
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
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderExpress);
    }
}

