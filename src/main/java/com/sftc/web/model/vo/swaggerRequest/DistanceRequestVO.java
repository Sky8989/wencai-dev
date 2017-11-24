package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "地址距离计算请求包装类")
public class DistanceRequestVO {
    @ApiModelProperty(name = "source",value = "寄件人经纬度",required = true)
    private SourceLLtudeVO source;
    @ApiModelProperty(name = "target",value = "收件人经纬度",required = true)
    private TargetLLtudeVO target;

    public SourceLLtudeVO getSource() {
        return source;
    }

    public void setSource(SourceLLtudeVO source) {
        this.source = source;
    }

    public TargetLLtudeVO getTarget() {
        return target;
    }

    public void setTarget(TargetLLtudeVO target) {
        this.target = target;
    }
}
