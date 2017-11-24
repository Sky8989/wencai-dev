package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "经纬度SetContant")
public class LLSetConstantVO {

    @ApiModelProperty(name = "MAX_LL_NUMBER",required = true)
    private int MAX_LL_NUMBER;
    @ApiModelProperty(name = "MIN_LL_NUMBER",required = true)
    private int MIN_LL_NUMBER;
    @ApiModelProperty(name = "RANGE_NUMBER",required = true)
    private double RANGE_NUMBER;

    @ApiModelProperty(name = "END_HOUR",required = true)
    private int END_HOUR;

    @ApiModelProperty(name = "BEGIN_HOUR",required = true)
    private int BEGIN_HOUR;

    public int getMAX_LL_NUMBER() {
        return MAX_LL_NUMBER;
    }

    public void setMAX_LL_NUMBER(int MAX_LL_NUMBER) {
        this.MAX_LL_NUMBER = MAX_LL_NUMBER;
    }

    public int getMIN_LL_NUMBER() {
        return MIN_LL_NUMBER;
    }

    public void setMIN_LL_NUMBER(int MIN_LL_NUMBER) {
        this.MIN_LL_NUMBER = MIN_LL_NUMBER;
    }

    public double getRANGE_NUMBER() {
        return RANGE_NUMBER;
    }

    public void setRANGE_NUMBER(double RANGE_NUMBER) {
        this.RANGE_NUMBER = RANGE_NUMBER;
    }

    public int getEND_HOUR() {
        return END_HOUR;
    }

    public void setEND_HOUR(int END_HOUR) {
        this.END_HOUR = END_HOUR;
    }

    public int getBEGIN_HOUR() {
        return BEGIN_HOUR;
    }

    public void setBEGIN_HOUR(int BEGIN_HOUR) {
        this.BEGIN_HOUR = BEGIN_HOUR;
    }
}
