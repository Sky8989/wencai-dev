package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.Orders;
import com.sftc.web.service.OrderService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.HEAD;
import java.lang.Object;
import java.util.ArrayList;
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

    /*
     * 普通提交订单 普通同城订单
     */

    public APIResponse placeOrder(Object object) {

        Long long_order_number= (long) (Math.random() * 100000 * 1000000);
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = JSONObject.fromObject(object);
        String str = gson.toJson(jsonObject1);
        System.out.println(str);
        try {
            Order order = new Order(time,long_order_number,
                    "待支付",
                    time,
                    (String)jsonObject1.getJSONObject("request").get("pay_type"),
            (String)jsonObject1.getJSONObject("request").get("product_type"),0.0,
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"),
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
            (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),
                    (String)jsonObject1.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),
                    (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),
                    (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                    "ORDER_BASIS_SAME",
                    Integer.parseInt((String)jsonObject1.getJSONObject("request").getJSONObject("order").get("sender_user_id")));
           System.out.println("aa");
            ValidateNull.validateParamT(jsonObject1,order);
            HttpPost post = new HttpPost(REQUEST_URL);
            post.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str, post);
            jsonObject = JSONObject.fromObject(res);
            if(jsonObject.get("errors")==null||jsonObject.get("error")==null) {
                if((String)jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time")!=null) {
                    orderMapper.addOrder(order);
                    OrderExpress orderExpress = new OrderExpress(time, long_order_number, (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("receiver"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("mobile"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("province"),
                            (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("city"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("region"), (String) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("address").get("street"), (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("weight"),//包裹类型
                            (String) jsonObject1.getJSONObject("request").getJSONArray("packages").getJSONObject(0).get("type"), "待支付", Integer.parseInt((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("sender_user_id")), order.getId(), (String) jsonObject.getJSONObject("request").get("uuid"), (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),
                            (Double) jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"));
                    if((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time")!=null){
                        orderExpress.setReserve_time((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time"));
                    }
                    orderExpressMapper.addOrderExpress(orderExpress);
                }
                jsonObject.put("order_id",order.getId());
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
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        List<OrderExpress> orderExpressList = orderParam.getOrderExpressList();
        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order(orderParam);
        try {
            long long_order_number = (long) (Math.random() * 100000 * 1000000);
            order.setOrder_number(long_order_number);
            //等待好友填写玩地址在判断是神秘同城还是神秘大网
            //order.setOrder_type("ORDER_FRIEND");
            System.out.println(orderParam.getSender_user_id());
            order.setSender_user_id(orderParam.getSender_user_id());
            orderMapper.addOrder(order);
            for (int i = 0; i < orderExpressList.size(); i++) {
                orderExpressList.get(i).setOrder_number(long_order_number);
                orderExpressList.get(i).setState("待好友填写");
                orderExpressList.get(i).setUuid("");
                orderExpressList.get(i).setSender_user_id(orderParam.getSender_user_id());
                orderExpressList.get(i).setReserve_time("");
                OrderExpress orderExpress = new OrderExpress(
                        orderExpressList.get(i).getOrder_number(),
                        orderExpressList.get(i).getPackage_type(),
                        orderExpressList.get(i).getObject_type(),
                        order.getId(),
                        order.getCreate_time(),
                        orderExpressList.get(i).getIs_use(),
                        orderExpressList.get(i).getState(),
                        orderExpressList.get(i).getUuid(),
                        orderExpressList.get(i).getSender_user_id(),
                        orderExpressList.get(i).getReserve_time()
                        );
                orderExpressMapper.addOrderExpress(orderExpress);
            }
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, order.getId());
    }


    /*
     * 好友填写寄件订单   将要分成两个接口  神秘同城 神秘大网
     */
    public synchronized APIResponse friendFillOrder(OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;
        //orderExpress.setUuid((String)jsonObject.getJSONObject("request").get("uuid"));
//            status = APIStatus.SUBMIT_FAIL;
        try {
            //???为何为空  已经修改数据库 已允许uuid为空
            //orderExpress.setUuid("");
            orderExpress.setState("好友已填写");
            orderExpressMapper.updateOrderExpress(orderExpress);

            //order的订单类型更新
        } catch (Exception e) {
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, orderExpress.getOrder_id());
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
    public APIResponse getOrderDetile(Order order, OrderExpress orderExpress, Token token) {
        APIStatus status = APIStatus.SUCCESS;

        JSONObject jsonObject = new JSONObject();

        try {

            String uuid = orderExpressMapper.getUuidByOrderId(orderExpress.getOrder_id());
            if (order.getOrder_type().equals("ORDER_FRIEND")) {

                Order order1 = orderMapper.orderAndOrderExpressAndGiftDetile(orderExpress.getOrder_id());
                jsonObject = JSONObject.fromObject(order1);
            } else {
                REQUESTS_URL = REQUESTS_URL + uuid;
                System.out.println(uuid);
                HttpGet get = new HttpGet(REQUESTS_URL);
                get.addHeader("PushEnvelope-Device-Token", token.getAccess_token());
                String res = APIGet.getGet(get);
                jsonObject = JSONObject.fromObject(res);
                REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
            }
        } catch (Exception e) {
            status = APIStatus.PARAMETER_FAIL;
            System.out.println(e.fillInStackTrace());
        }
        return APIUtil.getResponse(status, jsonObject);
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
    public APIResponse getMyOrderList(MyOrderParam myOrderParam) {
        APIStatus status = APIStatus.SUCCESS;
        String ORDERS_URL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        String uuids = "";
        List<OrderCallback> orderCallbacks = new ArrayList<OrderCallback>();
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        for (OrderExpress oe : orderExpressList) {
            uuids = uuids + oe.getUuid() + ",";
        }
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("uuid", uuids);
        try {
            List<Orders> orderses = APIResolve.getOrdersJson(ORDERS_URL, myOrderParam.getToken());
            for (Orders orders : orderses) {
                if (!orders.getStatus().equals("WAIT_FILL") || !orders.getStatus().equals("ALREADY_FILL")) {
                    String uuid = orders.getUuid();
                    String order_status = orders.getStatus();
                    orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
                }
            }
            if (myOrderParam.getState().equals("")) {
                myOrderParam.setState(null);
            } else {
                String[] arr_status = myOrderParam.getState().split(",");
                myOrderParam.setStates(arr_status);
            }
            myOrderParam.setPageNum(myOrderParam.getPageNum() - 1);
            orderCallbacks = orderExpressMapper.findMyOrderExpress(myOrderParam);
            for (OrderCallback orderCallback : orderCallbacks) {
                if (orderCallback.getOrder_type().equals("ORDER_FRIEND")) {
                    orderCallback.setUuid(null);
                }
                if (orderCallback.getGift_card_id() == 1) {
                    orderCallback.setIs_gift(true);
                } else {
                    orderCallback.setIs_gift(false);
                }
            }
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

            post.addHeader("PushEnvelope-Device-Token",(String)jsonObject1.getJSONObject("request").getJSONObject("merchant").get("access_token"));//97uAK7HQmDtsw5JMOqad
            String res = AIPPost.getPost(str,post);
             jsonObject = JSONObject.fromObject(res);

            OrderExpress orderExpress = new OrderExpress((String)jsonObject1.getJSONObject("request").getJSONObject("merchant").get("uuid"),Integer.parseInt((String)jsonObject1.getJSONObject("request").get("order_id")),
                    (Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),(Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),(String)jsonObject.getJSONObject("request").get("status"));
          if((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time")!=null){
              orderExpress.setReserve_time((String) jsonObject1.getJSONObject("request").getJSONObject("order").get("reserve_time"));
          }

            orderExpressMapper.updatePlace(orderExpress);
            Order order = new Order((Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("longitude"),(Double)jsonObject1.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").get("latitude"),Integer.parseInt((String)jsonObject1.getJSONObject("request").get("order_id")));
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
        try {
            PAY_URL = PAY_URL + (String) jsonObject1.getJSONObject("request").get("uuid") + "/attributes/merchant_comment";
            HttpPut put = new HttpPut(PAY_URL);
            put.addHeader("PushEnvelope-Device-Token", (String) jsonObject1.getJSONObject("request").get("access_token"));
            String res = AIPPost.getPost(str, put);
            jsonObject = JSONObject.fromObject(res);
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
       @Override
       public APIResponse timeConstants(APIRequest request) {
           APIStatus status = APIStatus.SUCCESS;
           JSONObject jsonObject = null;
           try {
               CONSTANTS_URL = CONSTANTS_URL+"constants/"+request.getParameter("constants")+"?latitude="+request.getParameter("latitude")+"&longitude="+request.getParameter("longitude");
               HttpGet get = new HttpGet(CONSTANTS_URL);
               get.addHeader("PushEnvelope-Device-Token",(String)request.getParameter("access_token"));
               String res =  APIGet.getGet(get);
               jsonObject = JSONObject.fromObject(res);
               if (jsonObject.get("errors")!=null||jsonObject.get("error")!=null){
                   status = APIStatus.CONSTANT_FALT;
               }
           } catch (Exception e) {
               System.out.println(e.fillInStackTrace());
               status = APIStatus.CONSTANT_FALT;
           }
           CONSTANTS_URL="http://api-dev.sf-rush.com/";
           return APIUtil.getResponse(status, jsonObject);
       }
       /*
        * @大网普通下单
        *
        * */
       public APIResponse createOrder(Object object) {
           //System.out.println("hxy:here comes a createOrder----------------------------");
           Long long_order_number= (long) (Math.random() * 100000 * 1000000);
           String orderid = APIRandomOrderId.getRandomString(9);
           APIStatus status = APIStatus.SUCCESS;
           JSONObject jsonObject = null;
           JSONObject jsonObject1 = JSONObject.fromObject(object);
           jsonObject1.getJSONObject("sf").put("orderid",orderid);
           String str = gson.toJson(jsonObject1.getJSONObject("sf"));
           //System.out.println("hxy:here shows a str");
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
               ValidateNull.validateParam(jsonObject1,order);
               //发送请求给sf，获取订单结果
               HttpPost post = new HttpPost(CREATEORDER_URL);
               String access_token =  APIGetToken.getToken();
               post.addHeader("Authorization", "bearer "+access_token);
               String res = AIPPost.getPost(str, post);
               //System.out.println(res);
               jsonObject = JSONObject.fromObject(res);
               if(jsonObject1.get("Message_Type")!=null) {
               if(jsonObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                   status = APIStatus.SUBMIT_FAIL;
               } }else {
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
                           order.getId(),orderid,0.00,
                           0.00
                   );
                   orderExpress.setReserve_time("");
                   //存储 订单快递信息
                   orderExpressMapper.addOrderExpress(orderExpress);
                   jsonObject.put("order_id",order.getId());
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
        JSONObject jsonObject1=null;
        try {
            HttpPost post = new HttpPost(COUNT_PRICE);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res = AIPPost.getPost(str, post);
             jsonObject1 = JSONObject.fromObject(res);
            if(jsonObject1.get("Message_Type")!=null) {
                if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.QUOTE_FAIL;
                }
            }
        }catch(Exception e){
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
        JSONObject jsonObject1=null;
        try {
            ORDERROUTE_URL = ORDERROUTE_URL+request.getParameter("orderid")+"&sort="+request.getParameter("sort");
            HttpGet get = new HttpGet(ORDERROUTE_URL);
            String access_token = APIGetToken.getToken();
            get.addHeader("Authorization", "bearer " + access_token);
            String res =APIGet.getGet(get);
            if(!res.equals("[]")){
                jsonObject1 = JSONObject.fromObject(res);
            }else {
                status = APIStatus.ORDERROUT_FALT;
            }

            if(jsonObject1.get("Message_Type")!=null) {
                if (jsonObject1.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            }
            ORDERROUTE_URL="http://api-c-test.sf-rush.com/api/sforderservice/OrderRouteQuery?orderid=";
        }catch(Exception e){
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
        jsonObject1.getJSONObject("sf").put("orderid",orderid);
        String str = gson.toJson(jsonObject1.getJSONObject("sf"));
        try {
            HttpPost post = new HttpPost(CREATEORDER_URL);
            String access_token = APIGetToken.getToken();
            post.addHeader("Authorization", "bearer " + access_token);
            String res =AIPPost.getPost(str,post);
            jsonObject = JSONObject.fromObject(res);
            if(jsonObject.get("Message_Type")!=null) {
                if (jsonObject.get("Message_Type").equals("ORDER_CREATE_ERROR")) {
                    status = APIStatus.ORDERROUT_FALT;
                }
            }else{
                OrderExpress orderExpress = new OrderExpress();
                orderExpress.setOrder_id(Integer.parseInt((String)jsonObject1.getJSONObject("order").get("order_id")));
            }
        }catch(Exception e){
            System.out.println(e.fillInStackTrace());
            status = APIStatus.ORDERROUT_FALT;
        }
        return APIUtil.getResponse(status, jsonObject);
    }
}

