package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 多包裹请求属性基类
 *
 * @author ： CatalpaFlat
 * @date ：Create in 13:38 2017/11/20
 */
@ApiModel(description = "多包裹请求属性基类")
public class MultiplePackageVO {
    @Getter
    @Setter
    @ApiModelProperty(name = "orderID", value = "订单id", example = "C1509504431473V4", required = true)
    @NotBlank(message = "orderID不能为空")
    private String orderID;

    @Getter
    @Setter
    @ApiModelProperty(name = "packagesVO", value = "包裹", required = true)
    private List<BatchPackagesVO> packagesVO;

    @Getter
    @Setter
    @ApiModelProperty(name = "product_type", value = "产品类型", example = "KUAISUDA", required = true)
    @NotBlank(message = "product_type不能为空")
    private String product_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "pay_type", value = "付款类型", example = "FREIGHT_PREPAID", required = true)
    @NotBlank(message = "pay_type不能为空")
    private String pay_type;

    @Getter
    @Setter
    @ApiModelProperty(name = "reserve_time", example = "1501497840000", required = true)
    @NotBlank(message = "reserve_time不能为空")
    private String reserve_time;
}
