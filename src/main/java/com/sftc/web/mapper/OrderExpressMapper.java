package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;

import java.util.List;

public interface OrderExpressMapper {

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpress(OrderExpress orderExpress);

    List<OrderExpress> findEmptyPackage(String order_number);
}
