package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by huxingyue on 2017/7/19.
 */
@ApiModel(value = "地址解析")
public class AddressResolution {
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("地址文本")
    private String address;

    @ApiModelProperty("经度")
    private double longitude;

    @ApiModelProperty("纬度")
    private double latitude;

    @ApiModelProperty("创建时间")
    private String create_time;

    public AddressResolution() {
    }

    public AddressResolution(String address, double longitude, double latitude) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.create_time = Long.toString(System.currentTimeMillis());
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public String getCreate_time() {return create_time;}

    public void setCreate_time(String create_time) {this.create_time = create_time;}
}
