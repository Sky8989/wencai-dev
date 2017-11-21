package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "注册用户邀请码")
public class RegisterInviteVO {
    @ApiModelProperty(name = "city",value = "城市",example = "深圳",required = true)
    private String city;
    @ApiModelProperty(name = "channel",example = "1",required = true)
    private String channel;

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getChannel() {return channel;}

    public void setChannel(String channel) {this.channel = channel;}
}
