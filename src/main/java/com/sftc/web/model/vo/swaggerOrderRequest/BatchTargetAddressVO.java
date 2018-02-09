package com.sftc.web.model.vo.swaggerOrderRequest;

import lombok.Getter;
import lombok.Setter;

/**
 * 收件人地址类
 *
 * @author ： CatalpaFlat
 * @date ：Create in 13:46 2017/11/20
 */
public class BatchTargetAddressVO {
    @Getter
    @Setter
    private String country;
    @Getter
    @Setter
    private String province;
    @Getter
    @Setter
    private String city;
    @Getter
    @Setter
    private String region;
    @Getter
    @Setter
    private String street;
    @Getter
    @Setter
    private String detail;
    @Getter
    @Setter
    private String receiver;
    @Getter
    @Setter
    private String mobile;
}
