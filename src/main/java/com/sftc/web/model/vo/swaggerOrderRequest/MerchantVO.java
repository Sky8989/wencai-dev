package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "信息验证包装类")
public class MerchantVO {
    @ApiModelProperty(name = "uuid",example = "2c9a85895d82ebe7015d8d4c6cc11df6",required = true)
    private String uuid;
    @ApiModelProperty(name = "access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
