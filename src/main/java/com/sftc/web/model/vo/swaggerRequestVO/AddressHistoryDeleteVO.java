package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/19.
 */
@ApiModel(value = "历史地址删除包装类")
public class AddressHistoryDeleteVO {
    @ApiModelProperty(name = "address_history_id",value = "地址簿id",example = "924",required = true)
    private String address_history_id;

    public String getAddress_history_id() {return address_history_id;}

    public void setAddress_history_id(String address_history_id) {this.address_history_id = address_history_id;}
}
