package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "用户手机号码信息")
public class UserMobileVO {

    @Getter @Setter
    @ApiModelProperty(name = "mobile",value = "手机",example = "13544185508",required = true)
    private String mobile;

    @Getter @Setter
    @ApiModelProperty(name = "attributes")
    private UserSMSAttributesVO attributes;

}
