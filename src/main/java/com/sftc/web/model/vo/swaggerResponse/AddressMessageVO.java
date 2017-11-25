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
@ApiModel(value = "地址信息对象")
public class AddressMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "Coupons",value = "优惠券列表")
    private List<Coupons> coupons;
}
