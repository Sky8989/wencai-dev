package com.sftc.web.model;

import com.sftc.web.model.entity.Order;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 取消订单类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class OrderCancel extends Object {

    private int id;
    // 创建时间
    private String create_time;
    // 取消原因
    private String reason;
    // 问题描述
    private String question_describe;
    // 取消的订单id
    private Order order;
    private String order_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getQuestion_describe() {
        return question_describe;
    }

    public void setQuestion_describe(String question_describe) {
        this.question_describe = question_describe;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public OrderCancel() {
    }

    public OrderCancel(HttpServletRequest request) {
        if (request.getParameter("order_id") != null && !"".equals(request.getParameter("order_id")))
        {this.order_id = request.getParameter("order_id");}
    }


}
