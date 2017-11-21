package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "SF登录请求包装类")
public class SFLoginRequestVO {
    @ApiModelProperty(name = "merchant")
    private TokenMerchantVO merchant;
    @ApiModelProperty(name = "message")
    private TokenMessageVO message;
    @ApiModelProperty(name = "token",value = "设备id",example = "token",required = true)
    private String token;

    public TokenMerchantVO getMerchant() {
        return merchant;
    }

    public void setMerchant(TokenMerchantVO merchant) {
        this.merchant = merchant;
    }

    public TokenMessageVO getMessage() {
        return message;
    }

    public void setMessage(TokenMessageVO message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
