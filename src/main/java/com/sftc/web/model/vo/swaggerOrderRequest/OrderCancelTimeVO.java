package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友多包裹超时单自动取消定时器")
public class OrderCancelTimeVO {
    @ApiModelProperty(name = "on", example = "1", required = true)
    private int on;
    @ApiModelProperty(name = "delay", example = "0", required = true)
    private int delay;
    @ApiModelProperty(name = "period", example = "21600", required = true)
    private int period;
    @ApiModelProperty(name = "timeOutInterval", example = "43200", required = true)
    private int timeOutInterval;

    public int getOn() {
        return on;
    }

    public void setOn(int on) {
        this.on = on;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTimeOutInterval() {
        return timeOutInterval;
    }

    public void setTimeOutInterval(int timeOutInterval) {
        this.timeOutInterval = timeOutInterval;
    }
}
