package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;

import java.util.List;

public interface OrderMapper {

    OrderCallback findOrderByOrderNumber(String order_number);

    void addOrderExpress(OrderExpress orderExpress);

    void addOrder(Order order);


    void updateOrder(Order order);
    //通过订单Id修改订单信息，只修改订单类型
    void updateOrderTypeById(Order Order);
    void updateOrderExpress(OrderExpress orderExpress);

    Order orderAndOrderExpressAndGiftDetile(int id);

    List<Order> myOrderList(Order order);


    int findPackageCount(String order_number);

    // C01 我的订单
    List<Order> myOrderLists(OrderExpress orderExpress);

    List<Order> myOrderListsForState(OrderExpress orderExpress);
    void updatePlace(Order order);
    Order placeOrderDetile(String uuid);
    void deleOrderAndOrderExpress(String uuid);

}
