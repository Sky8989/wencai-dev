package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价请求信息")
public class EvaluateMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "attributes",value = "评价内容",required = true)
    private EvaluateAttributesVO attributes;

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    private String uuid;

}
