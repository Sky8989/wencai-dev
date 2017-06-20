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
public class Evaluate extends Object {

    private int id;
    // 创建时间
    private String create_time;
    // 评价留言
    private String comments;
    // 送货速度
    private int fast_delivery;
    // 评价时间
    private int service_attitude;
    // 评价星级
    private int door_speed;
    // 评价人的用户id
    private User merchant;
    private int user_id;
    // 评价的是哪一个订单
    private Order order;
    private int order_id;

    public Evaluate(String comments, int fast_delivery, int service_attitude, int door_speed, User merchant, int user_id, int order_id) {
        this.comments = comments;
        this.fast_delivery = fast_delivery;
        this.service_attitude = service_attitude;
        this.door_speed = door_speed;
        this.merchant = merchant;
        this.user_id = user_id;
        this.order_id = order_id;
    }

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getFast_delivery() {
        return fast_delivery;
    }

    public void setFast_delivery(int fast_delivery) {
        this.fast_delivery = fast_delivery;
    }

    public int getService_attitude() {
        return service_attitude;
    }

    public void setService_attitude(int service_attitude) {
        this.service_attitude = service_attitude;
    }

    public int getDoor_speed() {
        return door_speed;
    }

    public void setDoor_speed(int door_speed) {
        this.door_speed = door_speed;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
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
