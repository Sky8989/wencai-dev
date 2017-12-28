package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.web.model.others.Coupons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "优惠券信息包装对象")
public class CouponsVO{

    @Getter @Setter
    @ApiModelProperty(name = "Coupons",value = "优惠券列表")
    private List<Coupons> coupons;
}
