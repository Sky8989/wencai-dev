package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.CourierMapper;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Courier;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private OrderMapper orderMapper;


    @Resource
    private OrderExpressMapper orderExpressMapper;

    /*
     * 普通提交订单
     */
    public APIResponse placeOrder(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order(request);
        try {
            orderMapper.addOrder(order);
            orderExpressMapper.addOrderExpress(new OrderExpress(request));
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, order.getOrder_number());
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
        Order order = new Order(request);
        try {
            orderMapper.addOrder(order);
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, order.getOrder_number());
    }


    /*
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String order_number = (String) request.getParameter("order_number");
        int order_package_count = orderMapper.findPackageCount(order_number);
        if (order_package_count > 0) {
            try {
                orderExpressMapper.addOrderExpress(new OrderExpress(request));
                orderMapper.updateOrder(new Order(order_number, order_package_count - 1));
            } catch (Exception e) {
                status = APIStatus.ORDER_SUBMIT_FAIL;
            }
        } else {
            status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
        }

        return APIUtil.getResponse(status, null);
    }
    /*
    * @查看所有订单
    * */
    @Override
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
        order.setUser_id(user_id);
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
    public APIResponse getOrderDetile(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String orderSn = (String)request.getParameter("orderSn");
        System.out.println(orderSn);
        Order order = orderMapper.orderDetile(orderSn);
        System.out.println(order);
        if(order==null){
            status = APIStatus.COURIER_NOT_FOUND;
        }
        return  APIUtil.getResponse(status, order);
    }
    /*
    * 修改订单接口
    * */
    public APIResponse updateOrder(APIRequest request,Order order,OrderExpress orderExpress) {
        APIStatus status = APIStatus.SUCCESS;
        System.out.println(order.getVoice());
        order.setOrder_number("22");
        orderMapper.updateOrder(order);
        orderMapper.updateOrderExpress(orderExpress);
        return  APIUtil.getResponse(status, null);
    }
 }
