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
public class CouponApply {

    private int id;
    // 优惠券内容
    private String activity_content;
    // 优惠券属性
    private String coupon_attribute;
    // 优惠券码
    private String coupon_code;
    // 优惠券类型
    private String coupon_type;
    // 立减
    private double minus;
    // 折扣
    private double disount;
    // 优惠券人数上限
    private int upper_limit;
    // 优惠券使用范围
    private String use_coupon_scope;
    // 优惠券开始时间
    private String gmt_start_create;
    // 优惠券结束时间
    private String gmt_expiry_create;
    // 是否与别的优惠券并行使用
    private String is_concurrent_use;
    // 是否针对首次下单的用户
    private String is_first_order_user;
    // 针对哪种支付方式的用户
    private String use_pay_method;
    // 针对哪种标签的用户
    private String use_coupon_label;
    // 同一个账户使用的次数
    private int use_vaild_number;
    // 审核状态
    private String check_state;

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

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
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

    public String getUse_coupon_scope() {
        return use_coupon_scope;
    }

    public void setUse_coupon_scope(String use_coupon_scope) {
        this.use_coupon_scope = use_coupon_scope;
    }

    public String getGmt_start_create() {
        return gmt_start_create;
    }

    public void setGmt_start_create(String gmt_start_create) {
        this.gmt_start_create = gmt_start_create;
    }

    public String getGmt_expiry_create() {
        return gmt_expiry_create;
    }

    public void setGmt_expiry_create(String gmt_expiry_create) {
        this.gmt_expiry_create = gmt_expiry_create;
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

    public String getUse_pay_method() {
        return use_pay_method;
    }

    public void setUse_pay_method(String use_pay_method) {
        this.use_pay_method = use_pay_method;
    }

    public String getUse_coupon_label() {
        return use_coupon_label;
    }

    public void setUse_coupon_label(String use_coupon_label) {
        this.use_coupon_label = use_coupon_label;
    }

    public int getUse_vaild_number() {
        return use_vaild_number;
    }

    public void setUse_vaild_number(int use_vaild_number) {
        this.use_vaild_number = use_vaild_number;
    }

    public String getCheck_state() {
        return check_state;
    }

    public void setCheck_state(String check_state) {
        this.check_state = check_state;
    }
}
