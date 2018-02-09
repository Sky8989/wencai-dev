package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单提交请求包装类")
public class FriendOrderRequestVO {

    @Getter @Setter
    @ApiModelProperty(name = "request",value = "好友同城订单提交request对象",required = true)
    private FriendRequestVO request;

    @Getter @Setter
    @ApiModelProperty(name = "order",value = "好友同城订单信息",required = true)
    private FriendOrderMessageVO order;
}
