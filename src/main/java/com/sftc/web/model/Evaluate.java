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
    private String evaluate_message;
    // 服务速度等级
    private int service_speed_level;
    //上门速度等级
    private int visit_speed_level;
    //派送速度等级
    private int send_speed_level;
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

    public String getEvaluate_message() {
        return evaluate_message;
    }

    public void setEvaluate_message(String evaluate_message) {
        this.evaluate_message = evaluate_message;
    }

    public int getService_speed_level() {
        return service_speed_level;
    }

    public void setService_speed_level(int service_speed_level) {
        this.service_speed_level = service_speed_level;
    }

    public int getVisit_speed_level() {
        return visit_speed_level;
    }

    public void setVisit_speed_level(int visit_speed_level) {
        this.visit_speed_level = visit_speed_level;
    }

    public int getSend_speed_level() {
        return send_speed_level;
    }

    public void setSend_speed_level(int send_speed_level) {
        this.send_speed_level = send_speed_level;
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
