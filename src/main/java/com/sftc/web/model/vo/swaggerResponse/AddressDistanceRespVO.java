package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequestVO.CoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "地址距离计算响应对象")
public class AddressDistanceRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "distance",value = "距离",example = "100.1",dataType = "double")
    private Double result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
