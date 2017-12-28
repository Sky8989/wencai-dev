package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "好友同城订单request对象")
public class FriendRequestVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "region", example = "68034a73fccc11e68c2e0242ac1a0504", required = true)
    @NotBlank(message = "region参数不能为空")
    private String region;

    @Getter
    @Setter
    @ApiModelProperty(name = "attributes", required = true)
    @NotNull(message = "attributes参数不能为空")
    private AttributesVO attributes;

    @Getter
    @Setter
    @ApiModelProperty(name = "quote", value = "计价对象", required = true)
    @NotNull(message = "quote参数不能为空")
    private QuoteVO quote;

    @Getter
    @Setter
    @ApiModelProperty(name = "packages", value = "包裹信息", required = true)
    @NotEmpty(message = "packages参数不能为空")
    private List<PackagesVO> packages;

    @Getter
    @Setter
    @ApiModelProperty(name = "product_type", value = "配送方式", example = "JISUDA", required = true)
    @NotBlank(message = "product_type参数不能为空")
    private String product_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "pay_type", value = "支付方式", example = "FREIGHT_COLLECT", required = true, notes = "FREIGHT_COLLECT 到付 FREIGHT_PREPAID 寄付")
    @NotBlank(message = "pay_type参数不能为空")
    private String pay_type;
}
