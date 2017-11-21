package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户拆包")
public class UserUnpackingVO {
    @ApiModelProperty(name = "order_id", value = "订单id",example = "1",required = true)
    private String order_id;
    @ApiModelProperty(name = "type", value = "0：判断是否拆包/1：进行拆包",example = "1/0",required = true)
    private int type;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
