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
    private String message;
    // 服务速度等级
    private int speed_service_level;
    // 上门速度等级
    private int speed_visit_level;
    // 派送速度等级
    private int speed_send_level;
    // 评价时间
    private String gmt_create;
    // 评价人的用户id
    private User user;
    private int user_id;
    // 评价的是哪一个订单
    private Order order;
    private int order_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSpeed_service_level() {
        return speed_service_level;
    }

    public void setSpeed_service_level(int speed_service_level) {
        this.speed_service_level = speed_service_level;
    }

    public int getSpeed_visit_level() {
        return speed_visit_level;
    }

    public void setSpeed_visit_level(int speed_visit_level) {
        this.speed_visit_level = speed_visit_level;
    }

    public int getSpeed_send_level() {
        return speed_send_level;
    }

    public void setSpeed_send_level(int speed_send_level) {
        this.speed_send_level = speed_send_level;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
