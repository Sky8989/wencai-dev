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
@ApiModel(value = "订单取消包装类")
public class OrderCancelVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C151203098716843",required = true)
    @NotBlank(message = "order_id参数不能为空")
    private String order_id;
}
