package com.sftc.web.mapper;

import com.sftc.web.model.Order;

public interface OrderMapper {

    void addOrder(Order order);

    void updateOrder(Order order);

    Order orderDetile(String orderSn);

    int findPackageCount(String order_number);
}
