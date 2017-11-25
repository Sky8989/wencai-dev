package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.displayMessage.MyOrderListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 我的订单列表界面展示类
 */
@ApiModel(value = "我的订单列表响应对象")
public class OrderListRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "orderList")
    private List<MyOrderListVO> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
