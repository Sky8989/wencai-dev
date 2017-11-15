package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "获取token请求包装类")
public class GetTokenRequestVO {
    @ApiModelProperty(name = "merchant")
    private TokenMerchantVO merchant;
    @ApiModelProperty(name = "message")
    private TokenMessageVO message;
    @ApiModelProperty(name = "refresh_token",value = "新token",example = "refresh_token",required = true)
    private String refresh_token;

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

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
