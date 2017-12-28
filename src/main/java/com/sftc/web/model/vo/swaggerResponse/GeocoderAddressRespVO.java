package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "地址解析响应对象")
public class GeocoderAddressRespVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "Coordinate",value = "经纬度")
    private CoordinateVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
