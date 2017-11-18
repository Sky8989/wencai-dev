package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "地址实体扩展类")
public class AddressDTO extends Address{

    @Getter
    @Setter
    @ApiModelProperty(name = "avatar",value = "头像")
    private String avatar;  // 头像
}
