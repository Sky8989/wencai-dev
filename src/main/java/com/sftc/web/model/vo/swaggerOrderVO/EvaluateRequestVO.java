package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价请求信息包装类")
public class EvaluateRequestVO {
    @ApiModelProperty(name = "request", required = true)
    private EvaluateRequest request;

    public EvaluateRequest getRequest() {return request;}

    public void setRequest(EvaluateRequest request) {this.request = request;}
}
