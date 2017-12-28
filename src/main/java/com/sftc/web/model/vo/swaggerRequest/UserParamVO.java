package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
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
@ApiModel(value = "用户信息请求包装类")
public class UserParamVO extends BaseVO {
    @ApiModelProperty(name = "bespoken_time",value = "用户id",example = "10093",hidden = true)
    private int id;
    @ApiModelProperty(name = "js_code",value = "用户id",example = "041u1Eid1d3bbv0nFyld1FSjid1u1EiW")
    private String js_code;
    @ApiModelProperty(name = "avatar",value = "用户id",example = "https://wx.qlogo.cn/mmopen/vi_32/3KfBP6rtNj6s8RUqBD2ZEpSxcGibmRSo7fP6MCXbFlibXq7xBPkibdOoF7eOWD2CQOQ0VabCKxe0gs7q4qshkzvhw/0")
    private String avatar;
    @ApiModelProperty(name = "name",value = "用户id",example = "Bingo?")
    private String name;

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

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }
}
