package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "价格说明")
public class PriceExplain {


    @ApiModelProperty(name = "城市", example = "深圳", required = true)
    @Setter
    @Getter
    private String city;

    /**
     * 距离定价
     */
    @ApiModelProperty(name = "价格说明", required = true)
    @Setter
    @Getter
    private String price_explain;

}
