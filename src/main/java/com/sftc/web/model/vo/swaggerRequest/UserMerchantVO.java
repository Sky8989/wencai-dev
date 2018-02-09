package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户修改手机信息")
public class UserMerchantVO {

    @ApiModelProperty(name = "name",value = "名称",example = "名称")
    private String name;
    @ApiModelProperty(name = "mobile",value = "手机",example = "13797393543")
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
