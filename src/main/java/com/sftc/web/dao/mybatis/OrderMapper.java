package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.vo.swaggerOrderRequest.MyOrderParamVO;
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
    void addOrder2(Order order);

    /**
     * 查询我的订单列表
     */
    List<Order> selectMyOrderList(MyOrderParamVO param);

    List<OrderDTO> selectMyOrderList2(MyOrderParamVO param);


    /**
     * 查询我的好友订单列表
     */
    List<OrderDTO> selectMyFriendOrderList(MyOrderParamVO param);

    /**
     * 根据订单id查询订单详情
     */
    OrderDTO selectOrderDetailByOrderId(String order_id);

    /**
     * 好友单包裹直返回已填写包裹
     */
    OrderDTO selectOrderAlreadyFill(String order_id);

    /**
     * 添加了行级锁和排他锁的订单详情查询
     */
    OrderDTO selectOrderDetailByOrderIdForUpdate(String order_id);

    /**
     * 根据快递id查询订单详情
     */
    OrderDTO selectOrderDetailByExpressId(int express_id);

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
    OrderDTO selectOrderDetailByUuid(@Param("uuid") String uuid);

    // 取消订单，更新is_cancel字段
    void updateCancelOrderById(@Param("id") String id);

    //  下面是cms系统用到的mapper
    List<Order> selectOrderByPage(Order order);
}
