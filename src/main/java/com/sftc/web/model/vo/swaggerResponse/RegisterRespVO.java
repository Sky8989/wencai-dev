package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "用户注册响应对象")
public class RegisterRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "result")
    private RegisterMessageVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
