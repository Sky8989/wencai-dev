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
public class CouponApplicationForm {
    private int id;
    //优惠券内容
    private String activityContent;
    //优惠券属性
    private String couponAttribute;
    //优惠券码
    private String couponCode;
    //立减
    private double minus;
    //折扣
    private double disount;
    //优惠券人数上限
    private int upperLimit;
    //优惠券使用范围
    private String couponUseRange;
    //优惠券开始时间
    private Date couponStartTime;
    //优惠券结束时间
    private Date couponEndTime;
    //是否与别的优惠券并行使用
    private String isConcurrentUse;
    //是否针对首次下单的用户
    private String isfirstOrderUser;
    //针对哪种支付方式的用户
    private String paymentMethodUser;
    //针对哪种标签的用户
    private String couponLabelUse;
    //同一个账户使用的次数
    private int vaildUseNumber;
    //审核状态
    private String checkState;
    //用户id
    private int userId ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getCouponAttribute() {
        return couponAttribute;
    }

    public void setCouponAttribute(String couponAttribute) {
        this.couponAttribute = couponAttribute;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getCouponUseRange() {
        return couponUseRange;
    }

    public void setCouponUseRange(String couponUseRange) {
        this.couponUseRange = couponUseRange;
    }

    public Date getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(Date couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public Date getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(Date couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getIsConcurrentUse() {
        return isConcurrentUse;
    }

    public void setIsConcurrentUse(String isConcurrentUse) {
        this.isConcurrentUse = isConcurrentUse;
    }

    public String getIsfirstOrderUser() {
        return isfirstOrderUser;
    }

    public void setIsfirstOrderUser(String isfirstOrderUser) {
        this.isfirstOrderUser = isfirstOrderUser;
    }

    public String getPaymentMethodUser() {
        return paymentMethodUser;
    }

    public void setPaymentMethodUser(String paymentMethodUser) {
        this.paymentMethodUser = paymentMethodUser;
    }

    public String getCouponLabelUse() {
        return couponLabelUse;
    }

    public void setCouponLabelUse(String couponLabelUse) {
        this.couponLabelUse = couponLabelUse;
    }

    public int getVaildUseNumber() {
        return vaildUseNumber;
    }

    public void setVaildUseNumber(int vaildUseNumber) {
        this.vaildUseNumber = vaildUseNumber;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
