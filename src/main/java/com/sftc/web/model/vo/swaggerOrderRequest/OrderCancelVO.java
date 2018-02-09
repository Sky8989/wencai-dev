package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单取消包装类")
public class OrderCancelVO {

    @Getter @Setter
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508231583834HV",required = true)
    private String order_id;
}
