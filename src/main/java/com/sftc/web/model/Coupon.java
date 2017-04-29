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
    private int state;
    // 优惠码类型表id
    private int coupon_apply_id;
    // 优惠券获得者
    private User user;
    private int user_id;
    // 用在哪个订单
    private Order order;
    private int order_id;
    //同一个账户使用的有效次数
    private int use_valid_number;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCoupon_apply_id() {
        return coupon_apply_id;
    }

    public void setCoupon_apply_id(int coupon_apply_id) {
        this.coupon_apply_id = coupon_apply_id;
    }

    public int getUse_valid_number() {
        return use_valid_number;
    }

    public void setUse_valid_number(int use_valid_number) {
        this.use_valid_number = use_valid_number;
    }
}
