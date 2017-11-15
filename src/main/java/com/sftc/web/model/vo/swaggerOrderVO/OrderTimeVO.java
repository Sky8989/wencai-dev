package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "大网预约单下单定时器")
public class OrderTimeVO {
    @ApiModelProperty(name = "on", example = "1", required = true)
    private int on;
    @ApiModelProperty(name = "delay", example = "0", required = true)
    private int delay;
    @ApiModelProperty(name = "period", example = "1800", required = true)
    private int period;

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
}
