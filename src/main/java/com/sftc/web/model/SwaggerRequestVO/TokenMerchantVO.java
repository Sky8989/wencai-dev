package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "获取token用户信息包装类")
public class TokenMerchantVO {
    @ApiModelProperty(name = "mobile",value = "用户手机",example = "yourPhone",required = true)
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
