package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单支付包装类")
public class OrderPayVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    @NotBlank(message = "uuid参数不能为空")
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "cash",value = "支付金额",example = "10")
    private Double cash;
}
