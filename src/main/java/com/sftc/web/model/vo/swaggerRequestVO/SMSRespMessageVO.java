package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "短信验证响应信息")
public class SMSRespMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "uuid",example = "2c9a85895fb9c8da015fd4e35eed12c4")
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "状态",example = "INIT")
    private String status;

    @Getter @Setter
    @ApiModelProperty(name = "type",value = "类型",example = "WX_REGISTER_VERIFY_SMS")
    private String type;

    @Getter @Setter
    @ApiModelProperty(name = "receiver",value = "接收手机号",example = "15869721771")
    private String receiver;

    @Getter @Setter
    @ApiModelProperty(name = "createdAt",value = "时间",example = "2017-11-19T23:26:31.661+0800")
    private String createdAt;

}
