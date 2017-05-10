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

    public APIResponse placeOrder(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order(request);
        OrderExpress orderExpress = new OrderExpress(request);
        try {
            orderMapper.addOrdinaryOrder(order);
            orderExpressMapper.addOrdinaryOrderExpress(orderExpress);
        } catch (Exception e) {
            status = APIStatus.ORDER_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse payOrder(APIRequest request) {

        APIStatus status = APIStatus.SUCCESS;
        Order order = new Order((String) request.getParameter("order_number"),
                Long.toString(System.currentTimeMillis()), "待揽件");
        try {
            orderMapper.updateOrder(order);
        } catch (Exception e) {
            status = APIStatus.ORDER_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, null);
    }
}
