package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "改变订单状态的请求包装类")
public class OrderStatusVO {
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508233636870TR")
    private String order_id;
    @ApiModelProperty(name = "status",value = "订单状态",example = "WAIT_HAND_OVER")
    private String status;

    public String getOrder_id() {return order_id;}

    public void setOrder_id(String order_id) {this.order_id = order_id;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
