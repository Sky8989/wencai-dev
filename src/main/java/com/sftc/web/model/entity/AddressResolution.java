package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxingyue on 2017/7/19.
 */
@ApiModel(value = "地址解析")
public class AddressResolution {
    @ApiModelProperty("主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty("地址文本")
    @Setter @Getter
    private String address;

    @ApiModelProperty("经度")
    @Setter @Getter
    private double longitude;

    @ApiModelProperty("纬度")
    @Setter @Getter
    private double latitude;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    public AddressResolution() {
    }

    public AddressResolution(String address, double longitude, double latitude) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.create_time = Long.toString(System.currentTimeMillis());
    }
}
