package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单计价请求对象")
public class PriceRequestVO extends BaseVO {
    @ApiModelProperty(name = "request",required = true)
    @NotNull(message = "request参数不能为空")
    private PriceRequest request;

    public PriceRequest getRequest() {return request;}

    public void setRequest(PriceRequest request) {this.request = request;}
}
