package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "改变快递状态的请求包装类")
public class OrderExpressStatusVO {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "route_state",value = "路由状态",example = "WAIT_HAND_OVER",required = true)
    private String route_state;

    @Getter @Setter
    @ApiModelProperty(name = "pay_state",value = "支付状态",example = "ALREADY_PAY",required = true)
    private String pay_state;
}
