package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "订单详情响应对象")
public class OrderDetailRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "merchant")
    private OrderDetailVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
