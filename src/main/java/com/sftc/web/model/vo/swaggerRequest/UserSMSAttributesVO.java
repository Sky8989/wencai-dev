package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户验证手机号Attributes")
public class UserSMSAttributesVO {

    @Getter @Setter
    @ApiModelProperty(name = "invite_code",value = "邀请码",example = "X0VRPX6V")
    private String invite_code;
}
