package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "注册用户邀请码")
public class RegisterRequestVO {
    @ApiModelProperty(name = "merchant",required = true)
    private RegisterMerchantVO merchant;
    @ApiModelProperty(name = "message",required = true)
    private UserMobileMessageVO message;
    @ApiModelProperty(name = "invite",required = true)
    private RegisterInviteVO invite;

    public RegisterMerchantVO getMerchant() {return merchant;}

    public void setMerchant(RegisterMerchantVO merchant) {this.merchant = merchant;}

    public UserMobileMessageVO getMessage() {return message;}

    public void setMessage(UserMobileMessageVO message) {this.message = message;}

    public RegisterInviteVO getInvite() {return invite;}

    public void setInvite(RegisterInviteVO invite) {this.invite = invite;}
}
