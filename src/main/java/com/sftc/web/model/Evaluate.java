package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 评价类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Evaluate {

    private int id;
    // 评价留言
    private String evaluateMessage;
    // 服务速度等级
    private int serviceSpeedLevel;
    //上门速度等级
    private int visitSpeedLevel;
    //派送速度等级
    private int sendSpeedLevel;
    // 评价人的用户id
    private User user;
    private int userId;
    // 评价的是哪一个订单
    private Order order;
    private int orderId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvaluateMessage() {
        return evaluateMessage;
    }

    public void setEvaluateMessage(String evaluateMessage) {
        this.evaluateMessage = evaluateMessage;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getServiceSpeedLevel() {
        return serviceSpeedLevel;
    }

    public void setServiceSpeedLevel(int serviceSpeedLevel) {
        this.serviceSpeedLevel = serviceSpeedLevel;
    }

    public int getVisitSpeedLevel() {
        return visitSpeedLevel;
    }

    public void setVisitSpeedLevel(int visitSpeedLevel) {
        this.visitSpeedLevel = visitSpeedLevel;
    }

    public int getSendSpeedLevel() {
        return sendSpeedLevel;
    }

    public void setSendSpeedLevel(int sendSpeedLevel) {
        this.sendSpeedLevel = sendSpeedLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
