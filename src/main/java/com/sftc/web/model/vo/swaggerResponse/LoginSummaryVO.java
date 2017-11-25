package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "登录返回summary")
public class LoginSummaryVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "grade",example = "0",dataType = "int")
    private int grade;

    @Getter @Setter
    @ApiModelProperty(name = "total",example = "0",dataType = "int")
    private int total;

    @Getter @Setter
    @ApiModelProperty(name = "five_star",example = "0",dataType = "int")
    private int five_star;

    @Getter @Setter
    @ApiModelProperty(name = "rated",example = "0",dataType = "int")
    private int rated;

    @Getter @Setter
    @ApiModelProperty(name = "commented",example = "0",dataType = "int")
    private int commented;
}
