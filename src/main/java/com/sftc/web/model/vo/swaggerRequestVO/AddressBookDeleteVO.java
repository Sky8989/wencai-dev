package com.sftc.web.model.vo.swaggerRequestVO;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/19.
 */
@ApiModel(value = "地址簿删除包装类")
public class AddressBookDeleteVO {
    @ApiModelProperty(name = "addressBook_id",value = "地址簿id",example = "924",required = true)
    private String addressBook_id;
    @ApiModelProperty(name = "address_book_type",value = "地址簿类型",example = "sender",required = true)
    private String address_book_type;

    public String getAddressBook_id() {return addressBook_id;}

    public void setAddressBook_id(String addressBook_id) {this.addressBook_id = addressBook_id;}

    public String getAddress_book_type() {return address_book_type;}

    public void setAddress_book_type(String address_book_type) {this.address_book_type = address_book_type;}
}
