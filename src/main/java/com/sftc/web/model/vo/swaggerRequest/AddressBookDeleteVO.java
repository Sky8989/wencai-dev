package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/19.
 */
@ApiModel(value = "地址簿删除包装类")
public class AddressBookDeleteVO {

    @Getter @Setter
    @ApiModelProperty(name = "addressBook_id",value = "地址簿id",example = "924",required = true)
    private String addressBook_id;

    @Getter @Setter
    @ApiModelProperty(name = "address_book_type",value = "地址簿类型",example = "sender",required = true)
    private String address_book_type;

}
