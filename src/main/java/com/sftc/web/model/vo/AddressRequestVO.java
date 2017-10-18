package com.sftc.web.model.vo;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "地址包装类")
public class AddressRequestVO {

    @ApiModelProperty(value = "地址名称",example = "悟空测试订单")
    private String name;

    @ApiModelProperty(value = "用户电话",example = "13066667777")
    private String phone;

    @ApiModelProperty(value = "省份",example = "广东")
    private String province;

    @ApiModelProperty(value = "城市",example = "深圳")
    private String city;

    @ApiModelProperty(value = "区域",example = "龙岗区")
    private String area;

    @ApiModelProperty(value = "详细地址",example = "龙城广场地铁站")
    private String address;

    @ApiModelProperty(value = "补充地址",example = "118号")
    private String supplementary_info;

    @ApiModelProperty(value = "经度",example = "114.260976")
    private double longitude;

    @ApiModelProperty(value = "纬度",example = "22.723223")
    private double latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
