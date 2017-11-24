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
    @ApiModelProperty(name = "uuid",value = "用户uuid",example = "2c9a85895d82ebe7015d8d4c6cc11df6",required = true)
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "优惠券状态",example = "all",required = true)
    private String status;
}
