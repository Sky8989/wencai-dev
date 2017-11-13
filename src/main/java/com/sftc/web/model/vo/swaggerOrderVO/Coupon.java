package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "优惠券uuid")
public class Coupon {
    @ApiModelProperty(name = "uuid",example = "")
    private String uuid;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}
}
