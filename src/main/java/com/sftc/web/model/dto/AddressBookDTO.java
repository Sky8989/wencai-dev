package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址关系总映射
 * Created by huxingyue on 2017/7/26.
 */
@ApiModel(value = "地址簿扩展类")
public class AddressBookDTO extends AddressBook {

    @Getter
    @Setter
    @ApiModelProperty(name = "address",value = "地址实体")
    private Address address;

    @Getter
    @Setter
    @ApiModelProperty(name = "addressDTO",value = "地址扩展类",hidden = true)
    private AddressDTO addressDTO;

    @Getter
    @Setter
    @ApiModelProperty(name = "ship_wechatname",value = "好友微信名")
    private String ship_wechatname;  // 好友微信名
}

