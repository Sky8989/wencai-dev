package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "收件人地址包装类")
public class OrderTargetAddressVO {

    @ApiModelProperty(value = "国家",example = "中国",required = true)
    private String country;

    @ApiModelProperty(value = "省份",example = "广东",required = true)
    private String province;

    @ApiModelProperty(value = "城市",example = "深圳",required = true)
    private String city;

    @ApiModelProperty(value = "区域",example = "南山区",required = true)
    private String region;

    @ApiModelProperty(value = "详细地址",example = "软件产业基地",required = true)
    private String street;

    @ApiModelProperty(value = "补充地址",example = "118号收件")
    private String supplementary_info;

    @ApiModelProperty(value = "邮编",example = "518000")
    private String zipcode;

    @ApiModelProperty(value = "收件人姓名",example = "小媛媛测试订单请勿")
    private String receiver;

    @ApiModelProperty(value = "寄件人电话",example = "13760237215",required = true)
    private String mobile;

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

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
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
}
