package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批量多包裹属性
 *
 * @author ： CatalpaFlat
 * @date ：Create in 17:44 2017/11/17
 */
@ApiModel(description = "批量多包裹属性")
public class BatchAttributesVO {
    @Getter
    @Setter
    @ApiModelProperty(name = "index", example = "1")
    private String index;
    @Getter
    @Setter
    @ApiModelProperty(name = "pay_in_group", example = "true")
    private Boolean pay_in_group;
}
