package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "注册用户邀请码")
public class RegisterAttributesVO {
    @ApiModelProperty(name = "invite_code",value = "邀请码",example = "1234",required = true)
    private String invite_code;

    public String getInvite_code() {return invite_code;}

    public void setInvite_code(String invite_code) {this.invite_code = invite_code;}
}
