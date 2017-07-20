package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    /**
     * 插入订单
     */
    void addOrder(Order order);

    /**
     * 更新订单区域类型
     */
    void updateOrderRegionType(@Param("id") int id, @Param("region_type") String region_type);

    /**
     * 查询我的订单列表
     */
    List<Order> selectMyOrderList(MyOrderParam param);

    /**
     * 查询我的好友订单列表
     */
    List<Order> selectMyFriendOrderList(MyOrderParam param);

    /**
     * 根据订单id查询订单详情
     */
    Order selectOrderDetailByOrderId(int order_id);

    /**
     * 根据快递id查询订单详情
     */
    Order selectOrderDetailByExpressId(int express_id);

    /**
     * 查询大网预约订单列表
     */
    List<Integer> selectNationReserveOrders();

    /**
     * 查询大网未提交的订单列表
     */
    List<Integer> selectNationUnCommitOrders();

    /**
     * 查询同城未提交的订单列表
     */
    List<Integer> selectSameUnCommitOrders();

    /**
     * 根据uuid查询订单详情
     */
    Order selectOrderDetailByUuid(@Param("uuid") String uuid);

    // 取消订单，更新is_cancel字段
    void updateCancelOrderById(int id);

    //     <!--下面是cms系统用到的mapper-->
    List<Order> selectOrderByPage(Order order);
}
