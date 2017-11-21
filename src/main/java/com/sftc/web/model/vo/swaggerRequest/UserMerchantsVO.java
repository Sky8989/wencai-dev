package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "更新用户信息包装类")
public class UserMerchantsVO {

    @ApiModelProperty(value = "用户name",example = "空城",required = true)
    private String name;

    @ApiModelProperty(value = "attributes")
    private UserMerchantsAttributesVO attributes;
    @ApiModelProperty(value = "summary")
    private UserMerchantsSummaryVO summary;
    @ApiModelProperty(value = "邮箱",example = "123@gmail.com",required = true)
    private String email;

    @ApiModelProperty(value = "地址",required = true)
    private UserMerchantsAddressVO address;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public UserMerchantsAttributesVO getAttributes() {return attributes;}

    public void setAttributes(UserMerchantsAttributesVO attributes) {this.attributes = attributes;}

    public UserMerchantsSummaryVO getSummary() {return summary;}

    public void setSummary(UserMerchantsSummaryVO summary) {this.summary = summary;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public UserMerchantsAddressVO getAddress() {
        return address;
    }

    public void setAddress(UserMerchantsAddressVO address) {
        this.address = address;
    }
}
