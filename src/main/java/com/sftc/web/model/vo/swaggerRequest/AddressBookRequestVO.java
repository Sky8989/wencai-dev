package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "地址簿包装类")
public class AddressBookRequestVO {

    @ApiModelProperty(value = "是否删除",example = "0")
    private int is_delete;
    @ApiModelProperty(value = "是否神秘",example = "0")
    private int is_mystery;
    @ApiModelProperty(value = "地址类型",example = "address_history")
    private String address_type;
    @ApiModelProperty(value = "地址簿类型",example = "sender")
    private String address_book_type;
    @ApiModelProperty(value = "地址类")
    private Address address;

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getIs_mystery() {
        return is_mystery;
    }

    public void setIs_mystery(int is_mystery) {
        this.is_mystery = is_mystery;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getAddress_book_type() {
        return address_book_type;
    }

    public void setAddress_book_type(String address_book_type) {
        this.address_book_type = address_book_type;
    }

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}
}
