package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户验证验证码请求包装类")
public class UserValidateVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "merchant",value = "用户手机",required = true)
    private UserMobileVO merchant;

    @Getter @Setter
    @ApiModelProperty(name = "message",value = "验证码信息",required = true)
    private UserSMSContentVO message;

    @Getter @Setter
    @ApiModelProperty(name = "invite",value = "邀请信息")
    private UserInviteVO invite;

}
