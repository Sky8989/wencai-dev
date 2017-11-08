package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "更新用户信息地址包装类")
public class UserMerchantsAddressVO {

    @ApiModelProperty(value = "用户uuid",example = "2c9a85895e99caa5015ebc09a1297072",required = true)
    private String uuid;
    @ApiModelProperty(value = "类型",example = "LIVE",required = true)
    private String type;
    @ApiModelProperty(value = "国家",example = "中国",required = true)
    private String country;
    @ApiModelProperty(value = "省份",example = "广东省",required = true)
    private String province;

    @ApiModelProperty(value = "城市",example = "深圳市",required = true)
    private String city;

    @ApiModelProperty(value = "区域",example = "龙岗区",required = true)
    private String region;

    @ApiModelProperty(value = "详细地址",example = "龙城广场地铁站",required = true)
    private String street;

    @ApiModelProperty(value = "marks")
    private UserMerchantsMarksVO marks;
    @ApiModelProperty(value = "邮编",example = "518000")
    private String zipcode;

    @ApiModelProperty(value = "收件人姓名",example = "near测试订单请勿")
    private String receiver;

    @ApiModelProperty(value = "寄件人电话",example = "18124033797",required = true)
    private String mobile;
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.94524651122737",required = true)
    private String longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.5304852403182",required = true)
    private String latitude;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public UserMerchantsMarksVO getMarks() {
        return marks;
    }

    public void setMarks(UserMerchantsMarksVO marks) {
        this.marks = marks;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
