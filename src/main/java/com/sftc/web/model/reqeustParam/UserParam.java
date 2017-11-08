package com.sftc.web.model.reqeustParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@ApiModel(value = "")
public class UserParam {
    @ApiModelProperty(name = "bespoken_time",value = "用户id",example = "10093",hidden = true)
    private int id;
    @ApiModelProperty(name = "token",value = "用户token",example = "EyMivbd44I124lcddrBG")
    private String token;
    @ApiModelProperty(name = "js_code",value = "用户id",example = "041u1Eid1d3bbv0nFyld1FSjid1u1EiW")
    private String js_code;
    @ApiModelProperty(name = "avatar",value = "用户id",example = "https://wx.qlogo.cn/mmopen/vi_32/3KfBP6rtNj6s8RUqBD2ZEpSxcGibmRSo7fP6MCXbFlibXq7xBPkibdOoF7eOWD2CQOQ0VabCKxe0gs7q4qshkzvhw/0")
    private String avatar;
    @ApiModelProperty(name = "name",value = "用户id",example = "Bingo?")
    private String name;
    @ApiModelProperty(name = "uuid",value = "用户id",example = "2c9a85895d82ebe7015d8d4c6cc11df6")
    private String uuid;

    public  String getAvatar() {
        return avatar;
    }
    public void   setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getName() {
        return name;
    }
    public void   setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}
}
