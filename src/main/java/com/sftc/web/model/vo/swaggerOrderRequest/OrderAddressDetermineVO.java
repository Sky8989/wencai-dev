package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.swaggerRequest.DetermineSourceVO;
import com.sftc.web.model.vo.swaggerRequest.DetermineTargetVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "判断是否同城请求信息")
public class OrderAddressDetermineVO {

    @Getter @Setter
    @ApiModelProperty(name = "source",value = "寄件人经纬度",required = true)
    private DetermineSourceVO source;

    @Getter @Setter
    @ApiModelProperty(name = "target",value = "收件人经纬度")
    private DetermineTargetVO target;

}
