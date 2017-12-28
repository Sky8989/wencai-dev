package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "地址簿包装类")
public class AddressBookRequestVO extends BaseVO{

    @ApiModelProperty(value = "是否删除",example = "0",required = true)
    @Getter @Setter
    private Integer is_delete;
    
    @ApiModelProperty(value = "是否神秘",example = "0",required = true)
    @Getter @Setter
    private Integer is_mystery;
    
    @ApiModelProperty(value = "地址类型",example = "address_history",required = true)
    @Getter @Setter
    private String address_type;
    
    @ApiModelProperty(value = "地址簿类型",example = "sender",required = true)
    @Getter @Setter
    private String address_book_type;
    
    @ApiModelProperty(value = "地址类")
    @Getter @Setter
    private Address address;

   
}
