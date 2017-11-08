package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "优惠券查询包装类")
public class CouponRequestVO {

    @ApiModelProperty(name = "uuid",value = "用户uuid",example = "2c9a85895d82ebe7015d8d4c6cc11df6")
    private String uuid;
    @ApiModelProperty(name = "token",value = "用户的access_token",example = "EyMivbd44I124lcddrBG")
    private String token;
    @ApiModelProperty(name = "status",value = "优惠券状态",example = "all")
    private String status;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
