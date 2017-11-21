package com.sftc.web.model.vo.displayVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 快递详情添加包裹信息
 *
 */
@ApiModel(value = "快递包裹信息包装类")
public class PackageMessageVO extends Object {
    @ApiModelProperty(name = "name",value = "名称",example = "0~5Kg")
    private String name;
    @ApiModelProperty(name = "weight",value = "重量",example = "3")
    private String weight;
    @ApiModelProperty(name = "type",value = "类型",example = "0")
    private String type;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getWeight() {return weight;}

    public void setWeight(String weight) {this.weight = weight;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
