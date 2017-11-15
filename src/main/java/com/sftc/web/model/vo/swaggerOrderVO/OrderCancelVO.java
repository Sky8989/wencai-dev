package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单取消包装类")
public class OrderCancelVO {
    @ApiModelProperty(name = "access_token",value = "顺丰access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508231583834HV",required = true)
    private String order_id;

    public String getAccess_token() {return access_token;}

    public void setAccess_token(String access_token) {this.access_token = access_token;}

    public String getOrder_id() {return order_id;}

    public void setOrder_id(String order_id) {this.order_id = order_id;}
}
