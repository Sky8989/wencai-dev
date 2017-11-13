package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "同城转大网的快递uuid")
public class OrderTransform {
    @ApiModelProperty(name = "uuid",example = "2c9a85895d97c789015d982f0b28023a",required = true)
    private String uuid;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}
}
