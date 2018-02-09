package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价的内容")
public class EvaluateAttributesVO {

    @Getter @Setter
    @ApiModelProperty(name = "merchant_comments", value = "评价内容", example = "测试评价内容", required = true)
    private String merchant_comments;

    @Getter @Setter
    @ApiModelProperty(name = "merchant_score", value = "评价标签", example = "5", required = true)
    private String merchant_score;

    @Getter @Setter
    @ApiModelProperty(name = "merchant_tags", value = "评价星级", example = "服务好评,速度超快", required = true)
    private String merchant_tags;

}
