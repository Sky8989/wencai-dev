package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "计价请求对象")
public class CountPriceRequestVO {
    @ApiModelProperty(name = "request",required = true)
    private PriceRequest request;

    public PriceRequest getRequest() {return request;}

    public void setRequest(PriceRequest request) {this.request = request;}
}
