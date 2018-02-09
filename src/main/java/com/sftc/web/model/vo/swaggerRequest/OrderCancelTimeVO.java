package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "取消同城订单定时器请求类")
public class OrderCancelTimeVO {

    @Getter @Setter
    @ApiModelProperty(name = "on",value = "开关参数 开 关 1/0",example = "1",dataType = "int")
    private int on;

    @Getter @Setter
    @ApiModelProperty(name = "period",value = "时间间隔",example = "21600000",dataType = "int")
    private int period;

    @Getter @Setter
    @ApiModelProperty(name = "delay",value = "超时时间间隔",example = "43200000",dataType = "int")
    private int delay;

}
