package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单信息")
public class FriendOrderMessageVO {
    @ApiModelProperty(name = "order_id",example = "C150841092812641")
    private String order_id;
    @ApiModelProperty(name = "reserve_time",value = "预约时间",example = "1501497840000")
    private String reserve_time;
    @ApiModelProperty(name = "form_id",value = "表单Id",example = "1501497840000")
    private String form_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }
}
