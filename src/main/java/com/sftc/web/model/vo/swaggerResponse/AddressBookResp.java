package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.web.model.entity.AddressBook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址关系总映射
 * Created by huxingyue on 2017/7/26.
 */
public class AddressBookResp extends AddressBook {

    @Getter
    @Setter
    @ApiModelProperty(name = "address", value = "地址信息", required = true)
    private AddressMessageVO address;

    @Getter
    @Setter
    @ApiModelProperty(name = "ship_wechatname", value = "好友微信名", example = "Reven", required = true)
    private String ship_wechatname;  // 好友微信名
}

