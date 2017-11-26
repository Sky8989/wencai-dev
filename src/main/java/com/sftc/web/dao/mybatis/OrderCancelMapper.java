package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderCancel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCancelMapper {

    /**
     * 获取取消订单的列表
     *
     * @param orderCancel 查询参数，有条件查询功能
     * @return 返回包含OrderCancel的list
     */
    List<OrderCancel> selectCanceledOrderList(OrderCancel orderCancel);
}
