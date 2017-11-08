package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友大网订单信息")
public class FriendNationOrderVO {
    @ApiModelProperty(name = "order_id",example = "C15082326694340O",required = true)
    private String order_id;
    @ApiModelProperty(name = "reserve_time",example = "",required = true)
    private String reserve_time;
    @ApiModelProperty(name = "package_count",example = "3",required = true)
    private String package_count;

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

    public String getPackage_count() {
        return package_count;
    }

    public void setPackage_count(String package_count) {
        this.package_count = package_count;
    }
}
