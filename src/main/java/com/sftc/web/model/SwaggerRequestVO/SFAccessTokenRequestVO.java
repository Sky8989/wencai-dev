package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "设置顺丰公共token请求包装类")
public class SFAccessTokenRequestVO {
    @ApiModelProperty(name = "access_token",value = "顺丰access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;

    public String getAccess_token() {return access_token;}

    public void setAccess_token(String access_token) {this.access_token = access_token;}
}
