package com.sftc.web.dao.mybatis;

import com.sftc.web.model.OrderCancel;

import java.util.List;

public interface OrderCancelMapper {

    /**
     * 添加取消订单的记录
     *
     * @param order 参数是取消的订单的id等重要信息
     */
    void addCancelOrder(OrderCancel order);

    /**
     * 获取取消订单的列表
     *
     * @param orderCancel 查询参数，有条件查询功能
     * @return 返回包含OrderCancel的list
     */
    List<OrderCancel> selectCanceledOrderList(OrderCancel orderCancel);
}
