package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "价格说明")
public class PriceExplain {
    @ApiModelProperty("城市")
    private String city;
    /**距离定价*/
    @ApiModelProperty("距离定价")
    private String distance_price;
    /**重量价格*/
    @ApiModelProperty("重量价格")
    private String weight_price;

    public String getDistance_price() {
        return distance_price;
    }

    public void setDistance_price(String distance_price) {
        this.distance_price = distance_price;
    }

    public String getWeight_price() {
        return weight_price;
    }

    public void setWeight_price(String weight_price) {
        this.weight_price = weight_price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
