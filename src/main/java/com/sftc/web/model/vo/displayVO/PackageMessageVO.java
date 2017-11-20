package com.sftc.web.model.vo.displayVO;

import com.sftc.web.enumeration.express.PackageType;

/**
 * 快递详情添加包裹信息
 *
 */
public class PackageMessageVO extends Object {
    private String name;
    private String weight;
    private PackageType type;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getWeight() {return weight;}

    public void setWeight(String weight) {this.weight = weight;}

    public PackageType getType() {return type;}

    public void setType(PackageType type) {this.type = type;}
}
