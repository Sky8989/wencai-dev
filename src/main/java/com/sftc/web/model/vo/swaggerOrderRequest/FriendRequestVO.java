package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友同城订单request对象")
public class FriendRequestVO {

    @Getter @Setter
    @ApiModelProperty(name = "region",example = "68034a73fccc11e68c2e0242ac1a0504",required = true)
    private String region;

    @Getter @Setter
    @ApiModelProperty(name = "attributes",required = true)
    private AttributesVO attributes;

    @Getter @Setter
    @ApiModelProperty(name = "friend_quote",value = "计价对象",required = true)
    private FriendQuoteVO quote;

    @Getter @Setter
    @ApiModelProperty(name = "packages",value = "包裹信息",required = true)
    private List<PackagesVO> packages;

    @Getter @Setter
    @ApiModelProperty(name = "product_type",value = "配送方式",example = "JISUDA",required = true)
    private String product_type;

    @Getter @Setter
    @ApiModelProperty(name = "pay_type",value = "支付方式",example = "FREIGHT_COLLECT",required = true,notes = "FREIGHT_COLLECT 到付 FREIGHT_PREPAID 寄付")
    private String pay_type;
}
