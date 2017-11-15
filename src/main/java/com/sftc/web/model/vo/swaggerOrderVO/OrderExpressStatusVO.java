package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "改变快递状态的请求包装类")
public class OrderExpressStatusVO {
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a")
    private String uuid;
    @ApiModelProperty(name = "status",value = "快递状态",example = "WAIT_HAND_OVER")
    private String status;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
