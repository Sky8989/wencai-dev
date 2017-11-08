package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.OrderExpressTransform;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderExpressMapper {

    List<OrderExpress> selectExpressForId(int id);

    void addOrderExpress(OrderExpress orderExpress);

    void addOrderExpress2(OrderExpress orderExpress);//多增加门牌号信息

    void updateOrderExpressForSF(OrderExpress orderExpress);

    void updateOrderExpressUuidAndReserveTimeById(@Param("id") int id, @Param("uuid") String uuid, @Param("reserve_time") String reserve_time);

    void updateOrderExpressUuidAndOrderNumberWithId(@Param("id") int id, @Param("uuid") String uuid, @Param("order_number") String order_number);

    /**
     * 根据id更改快递状态
     */
    void updateOrderExpressStatus(@Param("express_id") int express_id, @Param("status") String status);

    /**
     * 根据uuid更改订单状态
     */
    void updateOrderExpressStatusByUUID(@Param("uuid") String uuid, @Param("status") String status);

    //根据uuid更改订单的状态和取件码以及取件码的状态
    void updateExpressDirectedByUUID(@Param("uuid") String uuid, @Param("status") String status,
                                     @Param("directed_code") String directed_code, @Param("is_directed") int is_directed);

    //根据uuid更改订单的Attributes
    void updateExpressAttributeSByUUID(@Param("uuid") String uuid, @Param("attributes") String attributes);

    // 更改 快递状态 为 取消 CANCELED
//    void updateOrderExpressCanceled(String order_id);

    // 更改 快递状态 为 超时 OVERTIME
//    void updateOrderExpressOvertime(String order_id);

    // 更新快递信息，by 快递id，用于好友填写订单时回填信息
//    void updateOrderExpressByOrderExpressId(OrderExpress orderExpress);

    List<OrderExpress> findAllOrderExpressByOrderId(String order_id);

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

    //获取快递信息
//    OrderExpress selectOrderExpress(String orderId);

    /**
     * 批量查询快递信息
     *
     * @param orderIdList 订单id列表
     * @return 返回快递列表
     */
    List<OrderExpress> selectExpressForsyncSFExpressStatus(List<Integer> orderIdList);


    ////////////////////下面是cms系统用到的mapper//////////////////////////

    List<OrderExpress> selectOrderExpressByPage(OrderExpress orderExpress);

    /**
     * 获取大网兜底单列表
     *
     * @param orderExpressTransform 转单快递的模型
     * @return 返回的是orderExpressTransform
     */
    List<OrderExpressTransform> selectOrderExpressTransformList(OrderExpressTransform orderExpressTransform);


}
