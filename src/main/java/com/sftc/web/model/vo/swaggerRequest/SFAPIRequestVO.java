package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "设置顺丰专送API环境请求包装类")
public class SFAPIRequestVO {
    @ApiModelProperty(name = "environment",value = "环境参数",example = "dev/stage/product",required = true)
    private String environment;

    public String getEnvironment() {return environment;}

    public void setEnvironment(String environment) {this.environment = environment;}
}
