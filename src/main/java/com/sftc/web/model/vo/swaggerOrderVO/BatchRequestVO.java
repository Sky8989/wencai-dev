package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 批量计价/订单请求对象
 *
 * @author ： CatalpaFlat
 * @date ：Create in 17:37 2017/11/17
 */
@ApiModel(description = "批量计价/订单请求对象")
public class BatchRequestVO {

    @Getter
    @Setter
    @ApiModelProperty(name = "targets", value = "收件人列表", required = true)
    private List<TargetAddressVO> targets;

    @Getter
    @Setter
    @ApiModelProperty(name = "packagesVO", value = "包裹", required = true)
    private List<BatchPackagesVO> packagesVO;

    @Getter
    @Setter
    @ApiModelProperty(name = "attributes", required = true, value = "是否多包裹")
    private BatchAttributesVO attributes;

    @Getter
    @Setter
    @ApiModelProperty(name = "product_type", value = "产品类型", example = "JISUDA", required = true)
    @NotBlank(message = "product_type不能为空")
    private String product_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "pay_type", value = "付款类型", example = "FREIGHT_COLLECT", required = true)
    @NotBlank(message = "pay_type不能为空")
    private String pay_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "source", value = "寄件人信息", required = true)
    private SourceAddressVO source;

    @Getter
    @Setter
    @ApiModelProperty(name = "merchant", required = true)
    private MerchantVO merchant;

    @Getter
    @Setter
    @ApiModelProperty(name = "reserve_time", example = "1501497840000", required = true)
    @NotBlank(message = "reserve_time不能为空")
    private String reserve_time;
}
