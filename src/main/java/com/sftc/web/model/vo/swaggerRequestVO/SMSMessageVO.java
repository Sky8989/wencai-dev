package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "短信验证信息包装类")
public class SMSMessageVO {
    @ApiModelProperty(name = "type",value = "类型",example = "WX_REGISTER_VERIFY_SMS",required = true)
    private String type;
    @ApiModelProperty(name = "receiver",value = "电话号码",example = "yourPhone",required = true)
    private String mobile;

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}
}
