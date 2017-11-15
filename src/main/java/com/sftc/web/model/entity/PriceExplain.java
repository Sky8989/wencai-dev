package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel(value = "价格说明")
public class PriceExplain {
    @ApiModelProperty("城市")
    @Setter @Getter
    private String city;

    /**距离定价*/
    @ApiModelProperty("距离定价")
    @Setter @Getter
    private String distance_price;

    /**重量价格*/
    @ApiModelProperty("重量价格")
    @Setter @Getter
    private String weight_price;

    public PriceExplain(){}
}
