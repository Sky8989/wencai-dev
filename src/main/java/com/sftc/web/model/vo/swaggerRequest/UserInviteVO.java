package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户邀请者信息")
public class UserInviteVO {

    @Getter @Setter
    @ApiModelProperty(name = "city",value = "城市",example = "深圳")
    private String city;

    @Getter @Setter
    @ApiModelProperty(name = "channel",value = "渠道",example = "1")
    private String channel;
}
