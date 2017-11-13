package com.sftc.web.model.sfdo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class Promo {

    @ApiModelProperty(name = "promo_code",value = "兑换密语",example = "1")
    private String promo_code;
    @ApiModelProperty(name = "token",value = "token",example = "EyMivbd44I124lcddrBG")
    private String token;

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
