package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    List<OrderExpress> findEmptyPackage(String order_number);

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpress(OrderExpress orderExpress);

    void updateOrderExpressForSF(OrderExpress orderExpress);

    void updateOrderExpressUuidAndReserveTimeById(@Param("id") int id, @Param("uuid") String uuid, @Param("reserve_time") String reserve_time);

    void updatePlace(OrderExpress orderExpress);

    /**
     * 更改快递状态
     */
    void updateOrderExpressStatus(@Param("express_id") int express_id, @Param("status") String status);

    // 更改 快递状态 为 取消 CANCELED
    void  updateOrderExpressCanceled(int order_id);

    //更新快递信息，by 快递id，用于好友填写订单时回填信息
    void updateOrderExpressByOrderExpressId(OrderExpress orderExpress);

    String getUuidByOrderId(int order_id);

    //获取订单对应的未填写快递信息列表list,根据订单id order_id
    List<OrderExpress> UnWritenOrderExpressListByOrderIdAndShipnameNull(int order_id);

    List<OrderExpress> findAllOrderExpressByOrderId(int order_id);

    /**
     * 根据uuid查询快递
     */
    OrderExpress selectExpressByUuid(@Param("uuid") String uuid);
}
