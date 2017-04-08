package com.sftc.web.model;

import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 评价类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class CouponApply {

    private int id;
    // 优惠券内容
    private String activity_content;
    // 优惠券属性
    private String coupon_attribute;
    // 优惠券码
    private String coupon_code;
    // 立减
    private double minus;
    // 折扣
    private double disount;
    // 优惠券人数上限
    private int upper_limit;
    // 优惠券使用范围
    private String coupon_use_range;
    // 优惠券开始时间
    private Date coupon_start_time;
    // 优惠券结束时间
    private Date coupon_end_time;
    // 是否与别的优惠券并行使用
    private String is_concurrent_use;
    // 是否针对首次下单的用户
    private String is_first_order_user;
    // 针对哪种支付方式的用户
    private String payment_method_user;
    // 针对哪种标签的用户
    private String coupon_label_use;
    // 同一个账户使用的次数
    private int vaild_use_number;
    // 审核状态
    private String check_state;
    // 优惠券类型
    private String coupon_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getCoupon_attribute() {
        return coupon_attribute;
    }

    public void setCoupon_attribute(String coupon_attribute) {
        this.coupon_attribute = coupon_attribute;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public double getMinus() {
        return minus;
    }

    public void setMinus(double minus) {
        this.minus = minus;
    }

    public double getDisount() {
        return disount;
    }

    public void setDisount(double disount) {
        this.disount = disount;
    }

    public int getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(int upper_limit) {
        this.upper_limit = upper_limit;
    }

    public String getCoupon_use_range() {
        return coupon_use_range;
    }

    public void setCoupon_use_range(String coupon_use_range) {
        this.coupon_use_range = coupon_use_range;
    }

    public Date getCoupon_start_time() {
        return coupon_start_time;
    }

    public void setCoupon_start_time(Date coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public Date getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(Date coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }

    public String getIs_concurrent_use() {
        return is_concurrent_use;
    }

    public void setIs_concurrent_use(String is_concurrent_use) {
        this.is_concurrent_use = is_concurrent_use;
    }

    public String getIs_first_order_user() {
        return is_first_order_user;
    }

    public void setIs_first_order_user(String is_first_order_user) {
        this.is_first_order_user = is_first_order_user;
    }

    public String getPayment_method_user() {
        return payment_method_user;
    }

    public void setPayment_method_user(String payment_method_user) {
        this.payment_method_user = payment_method_user;
    }

    public String getCoupon_label_use() {
        return coupon_label_use;
    }

    public void setCoupon_label_use(String coupon_label_use) {
        this.coupon_label_use = coupon_label_use;
    }

    public int getVaild_use_number() {
        return vaild_use_number;
    }

    public void setVaild_use_number(int vaild_use_number) {
        this.vaild_use_number = vaild_use_number;
    }

    public String getCheck_state() {
        return check_state;
    }

    public void setCheck_state(String check_state) {
        this.check_state = check_state;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }
}
