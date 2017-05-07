package com.sftc.web.mapper;

import com.sftc.web.model.Order;

public interface OrderMapper {

    public void insertOrder(Order order);
    void updateOrder(Order order);
    Order orderDetile(String orderSn);
}
