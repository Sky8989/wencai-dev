package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;

import java.util.List;

public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpress(OrderExpress orderExpress);

    List<OrderExpress> findEmptyPackage(String order_number);

    void updateOrderExpressForSF(OrderExpress orderExpress);

    void updateOrderExpressUuidAndReserveTimeById(int id, String uuid, String reserve_time);

    void updatePlace(OrderExpress orderExpress);

    String getUuidByOrderId(int order_id);

    //获取订单对应的未填写快递信息列表list,根据订单id order_id
    List<OrderExpress> UnWritenOrderExpressListByOrderIdAndShipnameNull(int order_id);
    //更新快递信息，by 快递id，用于好友填写订单时回填信息
    void updateOrderExpressByOrderExpressId(OrderExpress orderExpress);

    List<OrderExpress> findAllOrderExpressByOrderId(int order_id);
}
