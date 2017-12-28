package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "更新通知消息已读请求包装类")
public class UpdateIsReadVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "message_id",value = "消息id",example = "141",required = true,dataType = "int")
    private int message_id;
}
