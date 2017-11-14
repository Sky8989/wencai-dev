package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "包裹类型")
public class PackagesVO {
    @ApiModelProperty(name = "type",value = "包裹类型",example = "FILE")
    private String type;
    @ApiModelProperty(name = "weight",value = "包裹重量",example = "3")
    private String weight;
    @ApiModelProperty(name = "comments",value = "包裹描述",example = "大家电测试包裹描述")
    private String comments;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
