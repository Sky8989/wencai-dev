package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressHistory;
import lombok.Getter;
import lombok.Setter;

public class AddressHistoryDTO extends AddressHistory {

    @Getter
    @Setter
    private String ship_wechatname; // 好友微信名

    @Getter
    @Setter
    private Address address; // 地址
}
