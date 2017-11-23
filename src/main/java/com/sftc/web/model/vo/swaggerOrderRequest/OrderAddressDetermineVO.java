package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.swaggerRequest.DetermineSourceVO;
import com.sftc.web.model.vo.swaggerRequest.DetermineTargetVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "判断是否同城请求包装类")
public class OrderAddressDetermineVO {

    @Getter @Setter
    @ApiModelProperty(name = "request",value = "request对象",required = true)
    private OrderAddressDetermine request;

}
