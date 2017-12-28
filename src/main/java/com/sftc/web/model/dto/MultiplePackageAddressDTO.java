package com.sftc.web.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 15:08 2017/11/20
 */
public class MultiplePackageAddressDTO {
    @Getter
    @Setter
    private String userUUId;
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
    @Getter
    @Setter
    private String country;

}
