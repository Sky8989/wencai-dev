package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.dto.OrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "更改订单状态响应对象")
public class OrderStateRespVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "merchant")
    private OrderDTO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
