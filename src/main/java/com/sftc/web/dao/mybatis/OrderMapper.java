package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.vo.swaggerOrderRequest.MyOrderParamVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    /**
     * 查询我的订单列表
     */
    List<OrderDTO> selectMyOrderList(MyOrderParamVO param);

    /**
     * 查询我的好友订单列表
     */
    List<OrderDTO> selectMyFriendOrderList(MyOrderParamVO param);

    /**
     * 根据订单id查询订单详情
     */
    OrderDTO selectOrderDetailByOrderId(String order_id);

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

    /**
     * 设置订单已取消
     */
    void updateCancelOrderById(@Param("id") String id);
}
