package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "同城订单提交请求包装类")
public class OrderRequestVO {
    @ApiModelProperty(name = "request",required = true)
    private RequestVO request;
    @ApiModelProperty(name = "order",required = true)
    private OrderMessage order;

    public RequestVO getRequest() {
        return request;
    }

    public void setRequest(RequestVO request) {
        this.request = request;
    }

    public OrderMessage getOrder() {
        return order;
    }

    public void setOrder(OrderMessage order) {
        this.order = order;
    }
}
