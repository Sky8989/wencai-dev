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
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.9369366556",dataType = "double",required = true)
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.5389728649",dataType = "double",required = true)
    private double latitude;

    @Getter @Setter
    @ApiModelProperty(name = "coordinate",value = "经纬度对象",hidden = true)
    private CoordinateVO coordinate;
}
