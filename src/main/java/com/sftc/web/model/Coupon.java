package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 优惠券类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Coupon {

    private int id;
    // 优惠券状态(是否使用)
    private int coupon_state;
    // 优惠券获得者
    private User user;
    private  int user_id;
    // 用在哪个订单
    private Order order;
    private int order_id;
    //同一个账户使用的有效次数
    private int vaild_use_number;

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

    public int getCoupon_state() {
        return coupon_state;
    }

    public void setCoupon_state(int coupon_state) {
        this.coupon_state = coupon_state;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getVaild_use_number() {
        return vaild_use_number;
    }

    public void setVaild_use_number(int vaild_use_number) {
        this.vaild_use_number = vaild_use_number;
    }
}
