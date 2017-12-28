package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel(value = "好友订单提交请求包装类")
public class FriendOrderRequestVO extends BaseVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "request", value = "好友同城订单提交request对象", required = true)
    @NotNull(message = "request参数不能为空")
    private FriendRequestVO request;

    @Getter
    @Setter
    @ApiModelProperty(name = "order", value = "好友同城订单信息", required = true)
    @NotNull(message = "order参数不能为空")
    private FriendOrderMessageVO order;
}
