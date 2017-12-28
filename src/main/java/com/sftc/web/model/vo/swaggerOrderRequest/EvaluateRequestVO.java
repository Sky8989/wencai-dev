package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价请求信息包装类")
public class EvaluateRequestVO extends BaseVO {
    @ApiModelProperty(name = "request", required = true)
    @NotNull
    private EvaluateMessageVO request;

    public EvaluateMessageVO getRequest() {return request;}

    public void setRequest(EvaluateMessageVO request) {this.request = request;}
}
