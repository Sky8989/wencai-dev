package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "根据uuid取消订单包装类")
public class OrderCancelVO2 {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895d8f0962015d8f19650a0045",required = true)
    private String uuid;
}
