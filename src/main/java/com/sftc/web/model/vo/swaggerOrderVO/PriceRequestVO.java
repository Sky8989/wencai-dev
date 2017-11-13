package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单计价请求对象")
public class PriceRequestVO {
    @ApiModelProperty(name = "request",required = true)
    private PriceRequest request;

    public PriceRequest getRequest() {return request;}

    public void setRequest(PriceRequest request) {this.request = request;}
}
