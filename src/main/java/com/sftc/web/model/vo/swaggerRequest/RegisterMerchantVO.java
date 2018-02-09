package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "注册的用户信息包装类")
public class RegisterMerchantVO {
    @ApiModelProperty(name = "mobile",value = "用户手机",example = "13272306247",required = true)
    private String mobile;
    @ApiModelProperty(name = "attributes",required = true)
    private RegisterAttributesVO attributes;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    public RegisterAttributesVO getAttributes() {return attributes;}

    public void setAttributes(RegisterAttributesVO attributes) {this.attributes = attributes;}
}
