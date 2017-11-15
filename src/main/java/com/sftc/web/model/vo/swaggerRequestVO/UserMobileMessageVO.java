package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户短信验证信息")
public class    UserMobileMessageVO {

    @ApiModelProperty(name = "type",value = "类型",example = "WX_REGISTER_VERIFY_SMS")
    private String type;
    @ApiModelProperty(name = "content",value = "验证码",example = "1234")
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
