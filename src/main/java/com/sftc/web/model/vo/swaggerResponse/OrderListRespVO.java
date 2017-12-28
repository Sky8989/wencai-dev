package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.dto.MyOrderListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 我的订单列表界面展示类
 */
@ApiModel(value = "我的订单列表响应对象")
public class OrderListRespVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "orderList")
    private List<MyOrderListDTO> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
