package com.sftc.web.mapper;

import com.sftc.web.model.Order;

public interface OrderMapper {
    void updateOrder(Order order);
    Order orderDetile(String orderSn);
    void addOrder(Order order);
}
