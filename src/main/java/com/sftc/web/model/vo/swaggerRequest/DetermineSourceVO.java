package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "判断同城寄件人经纬度")
public class DetermineSourceVO {

    @Getter @Setter
    @ApiModelProperty(name = "longitude",value = "经度",example = "114.260976",dataType = "double")
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.723223",dataType = "double")
    private double latitude;
}
