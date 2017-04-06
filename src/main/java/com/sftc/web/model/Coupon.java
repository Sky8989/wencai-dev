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
    private int couponState;
    // 优惠券获得者
    private User user;
    private  int userId;
    // 用在哪个订单
    private Order order;
    private int orderId;
    //同一个账户使用的有效次数
    private int vaildUseNumber;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getVaildUseNumber() {
        return vaildUseNumber;
    }

    public void setVaildUseNumber(int vaildUseNumber) {
        this.vaildUseNumber = vaildUseNumber;
    }

    public int getCouponState() {
        return couponState;
    }

    public void setCouponState(int couponState) {
        this.couponState = couponState;
    }


}
