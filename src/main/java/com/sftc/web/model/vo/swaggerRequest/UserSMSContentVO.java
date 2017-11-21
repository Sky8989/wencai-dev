package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户验证手机号信息")
public class UserSMSContentVO {

    @Getter @Setter
    @ApiModelProperty(name = "content",value = "验证码",example = "yourSMSCode")
    private String content;

    @Getter @Setter
    @ApiModelProperty(name = "type",value = "类型",example = "LOGIN_VERIFY_SMS")
    private String type;
}
