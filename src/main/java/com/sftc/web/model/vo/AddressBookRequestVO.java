package com.sftc.web.model.vo;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "地址簿包装类")
public class AddressBookRequestVO {

    @ApiModelProperty(value = "用户id",example = "10136")
    private int user_id;
    @ApiModelProperty(value = "是否删除",example = "0")
    private int is_delete;
    @ApiModelProperty(value = "是否神秘",example = "0")
    private int is_mystery;

    @ApiModelProperty(value = "地址类型",example = "address_history")
    private String address_type;
    @ApiModelProperty(value = "地址簿类型",example = "sender")
    private String address_book_type;
    @ApiModelProperty(value = "地址包装类")
    private AddressRequestVO addressRequestVO;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

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

    public AddressRequestVO getAddressRequestVO() {
        return addressRequestVO;
    }

    public void setAddressRequestVO(AddressRequestVO addressRequestVO) {
        this.addressRequestVO = addressRequestVO;
    }
}
