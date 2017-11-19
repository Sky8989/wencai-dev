package com.sftc.web.model.vo.swaggerResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "注册返回信息")
public class RegisterMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "merchant",value = "merchant")
    private LoginMerchantVO merchant;

    @Getter @Setter
    @ApiModelProperty(name = "token",value = "token")
    private RegisterTokenVO token;
}
