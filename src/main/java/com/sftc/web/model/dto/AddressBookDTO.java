package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressBook;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址关系总映射
 * Created by huxingyue on 2017/7/26.
 */
public class AddressBookDTO extends AddressBook {

    @Getter
    @Setter
    private Address address;

    @Getter
    @Setter
    private AddressDTO addressDTO;

    @Getter
    @Setter
    private String ship_wechatname;  // 好友微信名
}

