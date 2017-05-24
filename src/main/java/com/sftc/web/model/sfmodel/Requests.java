package com.sftc.web.model.sfmodel;


import com.sftc.web.model.*;
import com.sftc.web.model.Error;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/5/19.
 */
public class Requests {

    private Order order;

    private Request request;
    private OrderExpress orderExpress;

    public OrderExpress getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress orderExpress) {
        this.orderExpress = orderExpress;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
