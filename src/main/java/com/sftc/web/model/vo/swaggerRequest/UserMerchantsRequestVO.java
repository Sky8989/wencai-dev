package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "更新用户信息请求包装类")
public class UserMerchantsRequestVO {

    @ApiModelProperty(value = "attributes")
    private UserMerchantsVO merchant;

    public UserMerchantsVO getMerchant() {
        return merchant;
    }

    public void setMerchant(UserMerchantsVO merchant) {
        this.merchant = merchant;
    }
}
