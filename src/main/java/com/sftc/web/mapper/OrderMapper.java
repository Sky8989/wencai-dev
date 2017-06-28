package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    OrderCallback findOrderByOrderNumber(String order_number);

    void addOrderExpress(OrderExpress orderExpress);

    void addOrder(Order order);

    void updateOrder(Order order);

    void updateOrderRegionType(@Param("id") int id, @Param("region_type") String region_type);

    //通过订单Id修改订单信息，只修改订单类型
    void updateOrderTypeById(Order Order);

    void updateOrderExpress(OrderExpress orderExpress);

    Order orderAndOrderExpressAndGiftDetile(int id);

    /**
     * 查询我的订单列表
     */
    List<Order> selectMyOrderList(MyOrderParam param);

    /**
     * 查询我的好友订单列表
     */
    List<Order> selectMyFriendOrderList(MyOrderParam param);

    /** 根据订单编号查询订单详情 */
    Order selectOrderDetailByOrderNumber(String order_number);

    /** 根据订单id查询订单详情 */
    Order selectOrderDetailByOrderId(int order_id);

    /** 根据快递id查询订单详情 */
    Order selectOrderDetailByExpressId(int express_id);

    int findPackageCount(String order_number);

    List<Order> myOrderLists(OrderExpress orderExpress);

    List<Order> myOrderListsForState(OrderExpress orderExpress);

    void updatePlace(Order order);

    Order placeOrderDetail(@Param("uuid") String uuid);

    void deleOrderAndOrderExpress(String uuid);

    //查询订单详情和快递详情
    List<Order> getOrderAndExpress(Order order);
    //取消订单，更新is_cancel字段
    void updateCancelOrderById(int id);
}
