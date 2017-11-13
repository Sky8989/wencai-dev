package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "更新通知消息已读请求包装类")
public class UpdateIsReadVO {

    @ApiModelProperty(name = "message_id",value = "消息id",example = "141",required = true,dataType = "int")
    private int message_id;

    public int getMessage_id() {return message_id;}

    public void setMessage_id(int message_id) {this.message_id = message_id;}
}
