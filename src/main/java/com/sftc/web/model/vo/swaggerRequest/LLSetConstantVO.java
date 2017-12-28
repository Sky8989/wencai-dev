package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 * @author Administrator
 */
@ApiModel(value = "经纬度SetContant")
public class LLSetConstantVO extends BaseVO {

    @ApiModelProperty(name = "MAX_LL_NUMBER",required = true)
    private Integer MAX_LL_NUMBER;
    @ApiModelProperty(name = "MIN_LL_NUMBER",required = true)
    private Integer MIN_LL_NUMBER;
    @ApiModelProperty(name = "RANGE_NUMBER",required = true)
    private Double RANGE_NUMBER;

    @ApiModelProperty(name = "END_HOUR",required = true)
    private Integer END_HOUR;

    @ApiModelProperty(name = "BEGIN_HOUR",required = true)
    private Integer BEGIN_HOUR;

    public Integer getMAX_LL_NUMBER() {
        return MAX_LL_NUMBER;
    }

    public void setMAX_LL_NUMBER(Integer MAX_LL_NUMBER) {
        this.MAX_LL_NUMBER = MAX_LL_NUMBER;
    }

    public Integer getMIN_LL_NUMBER() {
        return MIN_LL_NUMBER;
    }

    public void setMIN_LL_NUMBER(Integer MIN_LL_NUMBER) {
        this.MIN_LL_NUMBER = MIN_LL_NUMBER;
    }

    public Double getRANGE_NUMBER() {
        return RANGE_NUMBER;
    }

    public void setRANGE_NUMBER(Double RANGE_NUMBER) {
        this.RANGE_NUMBER = RANGE_NUMBER;
    }

    public Integer getEND_HOUR() {
        return END_HOUR;
    }

    public void setEND_HOUR(Integer END_HOUR) {
        this.END_HOUR = END_HOUR;
    }

    public Integer getBEGIN_HOUR() {
        return BEGIN_HOUR;
    }

    public void setBEGIN_HOUR(Integer BEGIN_HOUR) {
        this.BEGIN_HOUR = BEGIN_HOUR;
    }

	@Override
	public String toString() {
		return "LLSetConstantVO [MAX_LL_NUMBER=" + MAX_LL_NUMBER + ", MIN_LL_NUMBER=" + MIN_LL_NUMBER
				+ ", RANGE_NUMBER=" + RANGE_NUMBER + ", END_HOUR=" + END_HOUR + ", BEGIN_HOUR=" + BEGIN_HOUR + "]";
	}
    
}
