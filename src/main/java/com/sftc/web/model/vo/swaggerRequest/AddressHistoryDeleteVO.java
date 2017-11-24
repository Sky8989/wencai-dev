package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/19.
 */
@ApiModel(value = "历史地址删除包装类")
public class AddressHistoryDeleteVO {

    @Getter @Setter
    @ApiModelProperty(name = "address_history_id",value = "地址簿id",example = "924",required = true)
    private String address_history_id;

}
