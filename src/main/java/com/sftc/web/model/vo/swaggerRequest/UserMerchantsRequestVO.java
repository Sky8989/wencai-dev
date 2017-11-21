package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "更新用户信息请求包装类")
public class UserMerchantsRequestVO {

    @ApiModelProperty(value = "顺丰token",example = "Ps0jFq9sjkPPbCQxH9zZ",required = true)
    private String token;

    @ApiModelProperty(value = "attributes")
    private UserMerchantsVO merchant;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserMerchantsVO getMerchant() {
        return merchant;
    }

    public void setMerchant(UserMerchantsVO merchant) {
        this.merchant = merchant;
    }
}
