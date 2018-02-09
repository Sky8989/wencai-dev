package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户新手机信息包装类")
public class UserNewMobileVO {

    @ApiModelProperty(name = "merchant",value = "用户信息")
    private UserMerchantVO merchant;
    @ApiModelProperty(name = "message",value = "验证码信息")
    private UserMobileMessageVO message;
    @ApiModelProperty(name = "user_id",value = "用户id",example = "10093")
    private String user_id;

    public UserMerchantVO getMerchant() {
        return merchant;
    }

    public void setMerchant(UserMerchantVO merchant) {
        this.merchant = merchant;
    }

    public UserMobileMessageVO getMessage() {
        return message;
    }

    public void setMessage(UserMobileMessageVO message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
