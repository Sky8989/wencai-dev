package com.sftc.web.model.SwaggerRequestVO;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/19.
 */
@ApiModel(value = "地址簿修改包装类")
public class AddressBookUpdateVO {
    @ApiModelProperty(name = "id",value = "地址簿id",example = "2351")
    private long id;
    @ApiModelProperty(value = "地址实体")
    private Address address;

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}
}
