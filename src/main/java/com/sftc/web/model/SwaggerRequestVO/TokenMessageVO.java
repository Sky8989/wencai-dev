package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "获取token信息包装类")
public class TokenMessageVO {
    @ApiModelProperty(name = "content",value = "验证码",example = "yourSMSCode",required = true)
    private String content;
    @ApiModelProperty(name = "type",value = "类型",example = "LOGIN_VERIFY_SMS",required = true)
    private String type;

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
