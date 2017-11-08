package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单attributes")
public class AttributesVO {
    @ApiModelProperty(name = "source",example = "DIRECTED")
    private String source;
    @ApiModelProperty(name = "bespoken_time",value = "时间",example = "2017-05-25T18:39:21.830+0800")
    private String bespoken_time;
    @ApiModelProperty(name = "description",value = "订单描述",example = "这是一条订单描述")
    private String description;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBespoken_time() {
        return bespoken_time;
    }

    public void setBespoken_time(String bespoken_time) {
        this.bespoken_time = bespoken_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
