package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.reqeustParam
 * @Description:
 * @date 2017/5/27
 * @Time 上午10:10
 */
@ApiModel(value = "APP登录请求信息包装类")
public class APPUserParamVO {

    @Getter @Setter
    @ApiModelProperty(name = "bespoken_time",value = "用户id",example = "10093",hidden = true)
    private int id;

    @Getter @Setter
    @ApiModelProperty(name = "token",value = "用户token",example = "EyMivbd44I124lcddrBG")
    private String token;

    @Getter @Setter
    @ApiModelProperty(name = "code",value = "用户id",example = "041u1Eid1d3bbv0nFyld1FSjid1u1EiW")
    private String code;

    @Getter @Setter
    @ApiModelProperty(name = "avatar",value = "用户id",example = "https://wx.qlogo.cn/mmopen/vi_32/3KfBP6rtNj6s8RUqBD2ZEpSxcGibmRSo7fP6MCXbFlibXq7xBPkibdOoF7eOWD2CQOQ0VabCKxe0gs7q4qshkzvhw/0")
    private String avatar;

    @Getter @Setter
    @ApiModelProperty(name = "name",value = "用户id",example = "Bingo?")
    private String name;

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "用户id",example = "2c9a85895d82ebe7015d8d4c6cc11df6")
    private String uuid;
}
