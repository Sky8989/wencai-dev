package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "订单相关用户信息包装类")
public class OrderMerchantVO {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "用户uuid",example = "17679122584")
    private String uuid;

}