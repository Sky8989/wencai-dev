package com.sftc.web.model.vo.swaggerRequestVO;

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
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.94524651122737",required = true,dataType = "double")
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.5304852403182",required = true,dataType = "double")
    private double latitude;

}
