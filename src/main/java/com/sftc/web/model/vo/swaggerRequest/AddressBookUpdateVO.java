package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by xf on 2017/10/19.
 */
@ApiIgnore
@ApiModel(value = "地址簿修改包装类")
public class AddressBookUpdateVO extends BaseVO {
    @ApiModelProperty(name = "id",value = "地址簿id",example = "3787",required = true)
    private Long id;
    @ApiModelProperty(value = "地址实体")
    private Address address;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

	@Override
	public String toString() {
		return "AddressBookUpdateVO [id=" + id + ", address=" + address + "]";
	}
    
    
}
