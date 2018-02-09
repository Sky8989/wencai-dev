package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "优惠券查询包装类")
public class CouponRequestVO {

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "优惠券状态",example = "all",required = true)
    private String status;
}
