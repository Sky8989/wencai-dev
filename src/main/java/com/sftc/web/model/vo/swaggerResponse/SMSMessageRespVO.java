package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.SMSRespMessageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "获取短信验证码响应对象")
public class SMSMessageRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "message",value = "短信验证信息")
    private SMSRespMessageVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
