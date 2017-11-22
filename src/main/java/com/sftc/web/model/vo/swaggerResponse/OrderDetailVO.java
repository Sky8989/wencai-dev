package com.sftc.web.model.vo.swaggerResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "订单详情信息包装类")
public class OrderDetailVO {

    @Getter @Setter
    @ApiModelProperty(name = "order",value = "订单信息")
    public OrderDetailMessageVO order;

}
