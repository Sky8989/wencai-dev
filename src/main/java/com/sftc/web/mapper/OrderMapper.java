package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;

import java.util.List;

public interface OrderMapper {


    void addOrderExpress(OrderExpress orderExpress);

    void addOrder(Order order);


    void updateOrder(Order order);
    void updateOrderExpress(OrderExpress orderExpress);

    Order orderAndOrderExpressAndGiftDetile(int id);

    List<Order> myOrderList(Order order);


    int findPackageCount(String order_number);

    // C01 我的订单
    List<Order> myOrderLists(OrderExpress orderExpress);

    List<Order> myOrderListsForState(OrderExpress orderExpress);
    void updatePlace(Order order);
    Order placeOrderDetile(int id);
}
