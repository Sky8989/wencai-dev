package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    /**
     * 插入订单
     */
//    void addOrder(Order order);

    /**
     * 插入订单 多增加门牌号信息
     */
//    void addOrder2(Order order);

    /**
     * 更新订单区域类型
     */
//    void updateOrderRegionType(@Param("id") String id, @Param("region_type") String region_type);

    /**
     * 查询我的订单列表
     */
    List<Order> selectMyOrderList(MyOrderParam param);

    List<OrderDTO> selectMyOrderList2(MyOrderParam param);


    /**
     * 查询我的好友订单列表
     */
    List<OrderDTO> selectMyFriendOrderList(MyOrderParam param);

    /**
     * 根据订单id查询订单详情
     */
    OrderDTO selectOrderDetailByOrderId(String order_id);

    /**
     * 添加了行级锁和排他锁的订单详情查询
     */
    OrderDTO selectOrderDetailByOrderIdForUpdate(String order_id);

    /**
     * 根据快递id查询订单详情
     */
    Order selectOrderDetailByExpressId(int express_id);

    /**
     * 查询大网预约订单列表
     */
    List<String> selectNationReserveOrders();

    /**
     * 查询大网未提交的订单列表
     */
    List<String> selectNationUnCommitOrders();

    /**
     * 查询好友多包裹未提交的订单列表
     */
    List<String> selectMutilExpressOrders();

    /**
     * 查询同城未提交的订单列表
     */
    List<String> selectSameUnCommitOrders();

    /**
     * 根据uuid查询订单详情
     */
    Order selectOrderDetailByUuid(@Param("uuid") String uuid);

    // 取消订单，更新is_cancel字段
//    void updateCancelOrderById(String id);


    //  下面是cms系统用到的mapper
    List<Order> selectOrderByPage(Order order);
}
