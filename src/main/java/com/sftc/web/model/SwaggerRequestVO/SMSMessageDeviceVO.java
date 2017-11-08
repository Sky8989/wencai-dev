package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "短信验证设备详细信息包装类")
public class SMSMessageDeviceVO {
    @ApiModelProperty(name = "detail",value = "设备详细信息",required = true)
    private SMSMessageDetailVO detail;

    public SMSMessageDetailVO getDetail() {return detail;}

    public void setDetail(SMSMessageDetailVO detail) {this.detail = detail;}
}
