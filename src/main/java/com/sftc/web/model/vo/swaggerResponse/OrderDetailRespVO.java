package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "订单列表响应对象")
public class OrderDetailRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "merchant")
    private OrderListVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
