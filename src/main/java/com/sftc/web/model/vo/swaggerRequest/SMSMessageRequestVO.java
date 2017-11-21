package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "获取短信验证请求包装类")
public class SMSMessageRequestVO {
    @ApiModelProperty(name = "message",value = "短信验证信息")
    private SMSMessageVO message;
    @ApiModelProperty(name = "device",value = "设备详细信息",required = true)
    private SMSMessageDeviceVO device;

    public SMSMessageVO getMessage() {return message;}

    public void setMessage(SMSMessageVO message) {this.message = message;}

    public SMSMessageDeviceVO getDevice() {return device;}

    public void setDevice(SMSMessageDeviceVO device) {this.device = device;}
}
