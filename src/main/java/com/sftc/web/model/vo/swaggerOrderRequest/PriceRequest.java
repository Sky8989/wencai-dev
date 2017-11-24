package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单计价request对象")
public class PriceRequest {

    @Getter @Setter
    @ApiModelProperty(name = "region",example = "68034a73fccc11e68c2e0242ac1a0504",required = true)
    private String region;

    @Getter @Setter
    @ApiModelProperty(name = "reserve_time",example = "1501497840000",required = true)
    private String reserve_time;

    @Getter @Setter
    @ApiModelProperty(name = "source",required = true)
    private SourceAddressVO source;

    @Getter @Setter
    @ApiModelProperty(name = "target",required = true)
    private TargetAddressVO target;

    @Getter @Setter
    @ApiModelProperty(name = "attributes",required = true)
    private AttributesVO attributes;

    @Getter @Setter
    @ApiModelProperty(name = "packages",required = true)
    private List<PackagesVO> packages;

    @Getter @Setter
    @ApiModelProperty(name = "product_type",example = "JISUDA",required = true)
    private String product_type;

    @Getter @Setter
    @ApiModelProperty(name = "pay_type",example = "FREIGHT_COLLECT",required = true)
    private String pay_type;
}
