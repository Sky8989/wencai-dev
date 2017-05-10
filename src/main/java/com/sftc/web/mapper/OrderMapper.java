package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;

public interface OrderMapper {

    void addOrderExpress(OrderExpress orderExpress);
    void updateOrder(Order order);
    Order orderDetile(String orderSn);
    void addOrder(Order order);
}
