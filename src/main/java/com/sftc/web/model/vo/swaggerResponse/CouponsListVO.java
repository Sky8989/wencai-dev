package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "优惠券列表响应对象")
public class CouponsListVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "Coupons",value = "优惠券列表")
    private CouponsVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
