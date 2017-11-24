package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "改变订单状态的请求包装类")
public class OrderStatusVO {

    @Getter @Setter
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508233636870TR",required = true)
    private String order_id;

    @Getter @Setter
    @ApiModelProperty(name = "route_state",value = "路由状态",example = "WAIT_HAND_OVER",required = true)
    private String route_state;

    @Getter @Setter
    @ApiModelProperty(name = "pay_state",value = "支付状态",example = "ALREADY_PAY",required = true)
    private String pay_state;

}
