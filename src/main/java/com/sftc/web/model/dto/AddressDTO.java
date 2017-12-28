package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import lombok.Getter;
import lombok.Setter;

public class AddressDTO extends Address {

    @Getter
    @Setter
    private String avatar;  // 头像
}
