package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;

import java.util.List;

public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    List<OrderCallback> findMyOrderExpress(MyOrderParam myOrderParam);

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpress(OrderExpress orderExpress);

    List<OrderExpress> findEmptyPackage(String order_number);

    void updateOrderExpressForSF(OrderExpress orderExpress);
    void updatePlace(OrderExpress orderExpress);
    String getUuidByOrderId(int order_id);
}
