package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.sfdo
 * @Description:
 * @date 2017/6/2
 * @Time 下午3:52
 */
@ApiModel(value = "优惠券兑换")
public class CouPonPromoVO {

    @Getter @Setter
    @ApiModelProperty(name = "promo_code",value = "兑换密语",example = "1",required = true)
    private String promo_code;
}
