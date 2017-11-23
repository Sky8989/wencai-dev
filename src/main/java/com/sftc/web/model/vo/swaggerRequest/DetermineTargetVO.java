package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "判断同城收件人经纬度")
public class DetermineTargetVO {

    @Getter @Setter
    @ApiModelProperty(name = "coordinate",value = "收件人经纬度")
    private CoordinateVO coordinate;
}
