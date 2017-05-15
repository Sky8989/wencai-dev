package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.reqeustParam.OrderParam;
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
        OrderParam orderParam = (OrderParam) request.getRequestParam();
        List<OrderExpress> orderExpressList = orderParam.getOrderExpressList();
        Order order = new Order(orderParam);
        try {
            orderMapper.addOrder(order);
            for (int i = 0; i < orderExpressList.size(); i++) {
                OrderExpress orderExpress = new OrderExpress(
                        order.getOrder_number(),
                        orderExpressList.get(i).getPackage_type(),
                        orderExpressList.get(i).getObject_type());
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
    public synchronized APIResponse friendFillOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        OrderExpress orderExpress = new OrderExpress(request);
        try {
            orderExpressMapper.updateOrderExpress(orderExpress);
        } catch (Exception e) {
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
        }
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
            status = APIStatus.ORDER_SUBMIT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, orderExpress);
    }
}
