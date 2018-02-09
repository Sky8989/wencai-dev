package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "收件人经纬度包装类")
public class TargetCoordinateVO {

    @Getter @Setter
    @ApiModelProperty(name = "longitude",value = "经度",example = "114.25885",dataType = "double",required = true)
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.72007",dataType = "double",required = true)
    private double latitude;
}
