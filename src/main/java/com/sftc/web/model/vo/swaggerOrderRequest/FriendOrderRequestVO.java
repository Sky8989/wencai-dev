package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单提交请求包装类")
public class FriendOrderRequestVO {
    @ApiModelProperty(name = "request",required = true)
    private FriendRequestVO request;
    @ApiModelProperty(name = "order",required = true)
    private FriendOrderMessageVO order;

    public FriendRequestVO getRequest() {return request;}

    public void setRequest(FriendRequestVO request) {this.request = request;}

    public FriendOrderMessageVO getOrder() {return order;}

    public void setOrder(FriendOrderMessageVO order) {this.order = order;}
}
