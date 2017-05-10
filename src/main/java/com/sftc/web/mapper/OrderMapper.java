package com.sftc.web.mapper;

import com.sftc.web.model.Order;

public interface OrderMapper {

    void addOrdinaryOrder(Order order);

    void updateOrder(Order order);

    Order orderDetile(String orderSn);
}
