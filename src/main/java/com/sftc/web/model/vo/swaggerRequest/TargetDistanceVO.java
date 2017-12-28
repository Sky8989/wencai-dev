package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "收件人经纬度")
public class TargetDistanceVO {

    @Getter @Setter
    @ApiModelProperty(name = "longitude",value = "经度",example = "114.26088879416709",dataType = "double",required = true)
    private Double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.72294328112597",dataType = "double",required = true)
    private Double latitude;

}
