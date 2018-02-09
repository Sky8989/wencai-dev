package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "实效计价请求包装类")
public class SFServiceRequestVO {
    @ApiModelProperty(name = "source",value = "寄件人区域",required = true)
    private SFServiceSourceVO source;
    @ApiModelProperty(name = "target",value = "收件人区域",required = true)
    private SFServiceSourceVO target;
    @ApiModelProperty(name = "weight",value = "重量",required = true,dataType = "int")
    private int weight;
    @ApiModelProperty(name = "query_time",value = "查询时间",example = "1499996089123")
    private String query_time;

    public SFServiceSourceVO getSource() {
        return source;
    }

    public void setSource(SFServiceSourceVO source) {
        this.source = source;
    }

    public SFServiceSourceVO getTarget() {
        return target;
    }

    public void setTarget(SFServiceSourceVO target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getQuery_time() {
        return query_time;
    }

    public void setQuery_time(String query_time) {
        this.query_time = query_time;
    }
}
