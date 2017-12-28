package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "计价信息")
public class QuoteVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "uuid", value = "计价信息uuid", example = "2c9a85895d97c789015d982f0b28023a", required = true)
    private String uuid;

    @Getter
    @Setter
    @ApiModelProperty(name = "coupon", value = "优惠券")
    private CouponVO coupon;
}
