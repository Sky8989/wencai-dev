package com.sftc.web.model.vo.displayVO;

/**
 * 快递详情添加包裹信息
 *
 */
public class PackageMessageVO extends Object {
    private String name;
    private String weight;
    private String type;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getWeight() {return weight;}

    public void setWeight(String weight) {this.weight = weight;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
