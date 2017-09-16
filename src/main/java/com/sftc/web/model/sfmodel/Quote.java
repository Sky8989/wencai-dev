package com.sftc.web.model.sfmodel;

/**
 * Created by Administrator on 2017/5/22.
 */
public class Quote {
    private String uuid;
    private Coupon coupon;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
