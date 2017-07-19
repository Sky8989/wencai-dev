package com.sftc.web.mapper;

import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    void addOrderExpress(OrderExpress orderExpress);

    void updateOrderExpressForSF(OrderExpress orderExpress);

    void updateOrderExpressUuidAndReserveTimeById(@Param("id") int id, @Param("uuid") String uuid, @Param("reserve_time") String reserve_time);

    /**
     * 根据id更改快递状态
     */
    void updateOrderExpressStatus(@Param("express_id") int express_id, @Param("status") String status);

    /**
     * 根据uuid更改订单状态
     */
    void updateOrderExpressStatusByUUID(@Param("uuid") int uuid, @Param("status") String status);

    // 更改 快递状态 为 取消 CANCELED
    void updateOrderExpressCanceled(int order_id);

    // 更新快递信息，by 快递id，用于好友填写订单时回填信息
    void updateOrderExpressByOrderExpressId(OrderExpress orderExpress);

    List<OrderExpress> findAllOrderExpressByOrderId(int order_id);

    /**
     * 根据uuid查询快递
     */
    OrderExpress selectExpressByUuid(@Param("uuid") String uuid);

    // 下单时，更新order_time,用于记录下单时间
    void updateOrderTime(@Param("uuid") String uuid, @Param("order_time") String order_time);

    // 更新订单的order_number为sf好友同城下单接口返回值 此id是快递信息的id
    void updateOrderNumber(@Param("id") int id, @Param("order_number") String order_number);

    //     <!--下面是cms系统用到的mapper-->
    List<OrderExpress> selectOrderExpressByPage(OrderExpress orderExpress);
}
