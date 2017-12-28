package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 *
 * @author Administrator
 */
@ApiModel(value = "地址距离计算请求包装类")
public class DistanceRequestVO extends BaseVO {
    @ApiModelProperty(name = "source", value = "寄件人经纬度", required = true)
    private SourceDistanceVO source;
    @ApiModelProperty(name = "target", value = "收件人经纬度", required = true)
    private TargetDistanceVO target;

    public SourceDistanceVO getSource() {
        return source;
    }

    public void setSource(SourceDistanceVO source) {
        this.source = source;
    }

    public TargetDistanceVO getTarget() {
        return target;
    }

    public void setTarget(TargetDistanceVO target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "DistanceRequestVO [source=" + source + ", target=" + target + "]";
    }

}
