package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "登录返回Attributes")
public class LoginAttributesVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "with_password",value = "是否包含密码",example = "false")
    private String with_password;
}
