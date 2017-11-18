package com.sftc.web.model.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/18.
 */
@ApiModel(value = "优惠券标题,限制信息")
public class CouponAttributes {

    @Getter @Setter
    @ApiModelProperty(name = "title",value = "标题",example = "测试")
    private String title;

    @Getter @Setter
    @ApiModelProperty(name = "limit_message",value = "限制信息",example = "限制标签")
    private String limit_message;
}
