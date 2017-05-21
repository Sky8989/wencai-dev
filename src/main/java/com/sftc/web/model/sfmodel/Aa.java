package com.sftc.web.model.sfmodel;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;

import java.util.Objects;

/**
 * Created by Administrator on 2017/5/21.
 */
public class Aa {
    private Order order;
    private OrderExpress orderExpress;
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderExpress getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress orderExpress) {
        this.orderExpress = orderExpress;
    }
}
