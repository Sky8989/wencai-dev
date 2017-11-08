package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友大网订单提交请求包装类")
public class FriendNationOrderRequestVO {
    @ApiModelProperty(name = "sf",required = true)
    private FriendNationSFVO sf;
    @ApiModelProperty(name = "order",required = true)
    private FriendNationOrderVO order;

    public FriendNationSFVO getSf() {
        return sf;
    }

    public void setSf(FriendNationSFVO sf) {
        this.sf = sf;
    }

    public FriendNationOrderVO getOrder() {
        return order;
    }

    public void setOrder(FriendNationOrderVO order) {
        this.order = order;
    }
}
