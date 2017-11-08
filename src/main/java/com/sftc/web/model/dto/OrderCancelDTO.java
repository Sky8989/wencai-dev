package com.sftc.web.model.dto;

import com.sftc.web.model.entity.OrderCancel;
import com.sftc.web.model.entity.Order;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 取消订单类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class OrderCancelDTO extends OrderCancel {


    // 取消的订单id
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderCancelDTO() {}

}
