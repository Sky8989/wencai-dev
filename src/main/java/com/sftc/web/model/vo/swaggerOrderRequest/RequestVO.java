package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "下单请求体")
public class RequestVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "region", example = "68034a73fccc11e68c2e0242ac1a0504", required = true)
    @NotBlank(message = "region参数不能为空")
    private String region;

    @Getter
    @Setter
    @ApiModelProperty(name = "request_source", value = "渠道 C_WX_APP",hidden = true)
    private String request_source;

    @Getter
    @Setter
    @ApiModelProperty(name = "type", value = "类型 NORMAL/RESERVED/DIRECTED",hidden = true)
    private String type;

    @Getter
    @Setter
    @ApiModelProperty(name = "merchant", value = "用户信息",hidden = true)
    private OrderMerchantVO merchant;

    @Getter
    @Setter
    @ApiModelProperty(name = "source", required = true)
    @NotNull(message = "source参数不能为空")
    private SourceAddressVO source;

    @Getter
    @Setter
    @ApiModelProperty(name = "target", required = true)
    @NotNull(message = "target参数不能为空")
    private TargetAddressVO target;

    @Getter
    @Setter
    @ApiModelProperty(name = "attributes", required = true)
    @NotNull(message = "attributes参数不能为空")
    private AttributesVO attributes;

    @Getter
    @Setter
    @ApiModelProperty(name = "quote", required = true)
    @NotNull(message = "quote参数不能为空")
    private QuoteVO quote;

    @Getter
    @Setter
    @ApiModelProperty(name = "packages", required = true)
    @NotEmpty(message = "packages参数不能为空")
    private List<PackagesVO> packages;

    @Getter
    @Setter
    @ApiModelProperty(name = "product_type", example = "JISUDA", required = true)
    @NotBlank(message = "product_type参数不能为空")
    private String product_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "pay_type", example = "FREIGHT_COLLECT", required = true)
    @NotBlank(message = "pay_type参数不能为空")
    private String pay_type;
}
