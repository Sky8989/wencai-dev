package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "设置兜底信息已读的请求包装类")
public class OrderTransformIsReadVO {
    @ApiModelProperty(name = "express_transform_id",value = "快递兜底id",example = "225")
    private String express_transform_id;

    public String getExpress_transform_id() {return express_transform_id;}

    public void setExpress_transform_id(String express_transform_id) {this.express_transform_id = express_transform_id;}
}
