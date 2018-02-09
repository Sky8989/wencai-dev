package com.sftc.web.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ： CatalpaFlat
 * @date ：Create in 14:26 2017/11/20
 */
public class MultiplePackageDTO {

    @Getter
    @Setter
    private String orderId;
    @Getter
    @Setter
    private String orderExpressId;
    @Getter
    @Setter
    private String quoteUUId;


    @Getter
    @Setter
    private MultiplePackageAddressDTO multiplePackageAddressDTO;
    @Getter
    @Setter
    private MultiplePackageLLDTO multiplePackageLLDTO;
}
