package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.OrderExpress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "更改快递状态响应对象")
public class OrderExpressStateRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "merchant")
    private OrderExpress result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
