package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderExpress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderExpressMapper {

    void addOrderExpress2(OrderExpress orderExpress);//多增加门牌号信息

    void updateOrderExpressUuidAndReserveTimeById(@Param("id") int id, @Param("uuid") String uuid, @Param("reserve_time") String reserve_time);

    /**
     * 根据id更改快递状态
     */
    void updateOrderExpressStatus(@Param("express_id") int express_id, @Param("status") String status,@Param("pay_state") String pay_state);

    /**
     * 根据uuid更改订单状态
     */
    void updateOrderExpressStatusByUUID(@Param("uuid") String uuid,
                                        @Param("route_state") String route_state,
                                        @Param("pay_state")String pay_state);

    //根据uuid更改订单的状态和取件码以及取件码的状态
    void updateExpressDirectedByUUID(@Param("uuid") String uuid, @Param("status") String status,
                                     @Param("directed_code") String directed_code, @Param("is_directed") int is_directed);

    //根据 uuid 更改订单的 Attributes 和状态
    void updateAttributesAndStatusByUUID(@Param("uuid") String uuid,@Param("attributes") String attributes,
                                         @Param("status") String status,@Param("pay_state") String pay_state);

    List<OrderExpress> findAllOrderExpressByOrderId(String order_id);

    void updatePayState (@Param("pay_state")String pay_state,
                         @Param("uuid")String uuid);

    /**
     * 根据uuid查询快递
     */
    OrderExpress selectExpressByUuid(@Param("uuid") String uuid);

    // 下单时，更新order_time,用于记录下单时间
    void updateOrderTime(@Param("uuid") String uuid, @Param("order_time") String order_time);

    // 更新订单的order_number为sf好友同城下单接口返回值 此id是快递信息的id
    void updateOrderNumber(@Param("id") int id, @Param("order_number") String order_number);


    /**
     * 用于同步sf订单状态
     * 获取该用户需要查询的订单的id
     *
     * @param user_id 用户id
     * @return 返回订单id的列表
     */
    List<Integer> selectOrderIdForsyncSFExpressStatus(int user_id);

    //查询 来往记录所需的快递
    List<OrderExpress> selectExpressForContactInfo(@Param("sender_user_id") int sender_user_id, @Param("ship_user_id") int ship_user_id);

    /**
     * 批量查询快递信息
     *
     * @param orderIdList 订单id列表
     * @return 返回快递列表
     */
    List<OrderExpress> selectExpressForsyncSFExpressStatus(List<Integer> orderIdList);

}
