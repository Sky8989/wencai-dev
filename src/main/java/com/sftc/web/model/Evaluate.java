package com.sftc.web.model;

import javax.servlet.http.HttpServletRequest;

/**
*@Author:hxy starmoon1994
*@Description:
*@Date:16:25 2017/6/28
*/
public class Evaluate extends Object {

    private int id;
    // 评价内容
    private String merchant_comments;
    // 评价分数
    private String merchant_score;
    // 标签的内容，逗号隔开
    private String merchant_tags;
    // 订单id
    private int order_id;
    // 评价人的用户id
    private int user_id;
    // 快递的id orderExpress_id
    private int orderExpress_id;
    // 快递的uuid
    private String uuid;
    // 创建时间
    private String create_time;

    public Evaluate() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchant_comments() {
        return merchant_comments;
    }

    public void setMerchant_comments(String merchant_comments) {
        this.merchant_comments = merchant_comments;
    }

    public String getMerchant_score() {
        return merchant_score;
    }

    public void setMerchant_score(String merchant_score) {
        this.merchant_score = merchant_score;
    }

    public String getMerchant_tags() {
        return merchant_tags;
    }

    public void setMerchant_tags(String merchant_tags) {
        this.merchant_tags = merchant_tags;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getOrderExpress_id() {return orderExpress_id;}

    public void setOrderExpress_id(int orderExpress_id) {this.orderExpress_id = orderExpress_id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public Evaluate(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("uuid") != null && !"".equals(request.getParameter("uuid"))) {
            this.uuid = request.getParameter("uuid");
        }
        if (request.getParameter("user_id") != null && !"".equals(request.getParameter("user_id"))) {
            this.user_id = Integer.parseInt(request.getParameter("user_id"));
        }
    }
}
