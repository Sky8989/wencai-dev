package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "注册返回token")
public class RegisterTokenVO extends APIResponse{
    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "uuid",example = "2c9a85895cf1d58d015cf81be46f1711")
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "access_token",value = "access_token",example = "mMusHzh0CuCXdjFtfmTR")
    private String access_token;

    @Getter @Setter
    @ApiModelProperty(name = "expires_in",value = "expires_in",example = "2592000")
    private String expires_in;

    @Getter @Setter
    @ApiModelProperty(name = "refresh_token",value = "refresh_token",example = "seN6s16GZI8Mx0iHQ2Fb")
    private String refresh_token;

    @Getter @Setter
    @ApiModelProperty(name = "created_at",value = "创建时间",example = "2017-06-30T16:26:36.527+0800")
    private String created_at;
}
