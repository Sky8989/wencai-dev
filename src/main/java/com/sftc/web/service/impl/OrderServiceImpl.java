package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 订单操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
@Service
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
        OrderExpress orderExpress = new OrderExpress(request);
        try {
            orderMapper.addOrder(order);
            orderExpressMapper.addOrderExpress(orderExpress);
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            OrderExpress orderExpress = new OrderExpress(request);
            try {
                orderExpressMapper.addOrderExpress(orderExpress);
                orderMapper.updateOrder(new Order(order_number, order_package_count - 1));
            } catch (Exception e) {
                status = APIStatus.ORDER_SUBMIT_FAIL;
                e.printStackTrace();
            }
        } else {
            status = APIStatus.ORDER_PACKAGE_COUNT_PULL;
        }
        return APIUtil.getResponse(status, null);
    }
}
