package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;

import java.util.List;

public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    List<OrderCallback> findMyOrderExpress(OrderExpress orderExpress);

    List<OrderCallback> findMyOrderExpressByOrderNumber(OrderCallback orderCallback);

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpress(OrderExpress orderExpress);

    List<OrderExpress> findEmptyPackage(String order_number);

    void updateOrderExpressForSF(OrderExpress orderExpress);
}
