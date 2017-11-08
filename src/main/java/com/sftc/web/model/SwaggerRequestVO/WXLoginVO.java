package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "微信用户刷新token请求包装类")
public class WXLoginVO {

    @ApiModelProperty(name = "unionId",value = "jscode",example = "WXJscode",required = true)
    private String unionId;

    public String getUnionId() {return unionId;}

    public void setUnionId(String unionId) {this.unionId = unionId;}
}
