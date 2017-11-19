package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequestVO.CoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "随机经纬度响应对象")
public class RandomLocationRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "Coordinate",value = "经纬度")
    private List<CoordinateVO> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
