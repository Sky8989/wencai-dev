package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友单验证uuid")
public class FriendQuote {
    @ApiModelProperty(name = "uuid",example = "2c9a85895d97c789015d982f0b28023a",required = true)
    private String uuid;
    @ApiModelProperty(name = "coupon",required = true)
    private Coupon coupon;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
