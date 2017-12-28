package com.sftc.web.model.vo.swaggerOrderRequest;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "优惠券")
public class CouponVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "uuid", value = "优惠券uuid", example = "2c9a85895f24480d015f28a798b33db3", required = true)
    private String uuid;
}