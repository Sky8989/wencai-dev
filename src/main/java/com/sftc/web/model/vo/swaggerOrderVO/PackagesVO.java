package com.sftc.web.model.vo.swaggerOrderVO;

import com.sftc.web.enumeration.express.ObjectType;
import com.sftc.web.enumeration.express.PackageType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "包裹类型")
public class PackagesVO {
    @ApiModelProperty(name = "type",value = "包裹类型",example = "FILE")
    private ObjectType type;
//    private String type;
    @ApiModelProperty(name = "weight",value = "包裹重量",example = "3")
    private String weight;
    @ApiModelProperty(name = "package_type",value = "包裹大小类型",example = "0")
    private PackageType package_type;
//    private String package_type;
    @ApiModelProperty(name = "comments",value = "包裹描述",example = "大家电测试包裹描述")
    private String comments;

   /* public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }*/

    /*public String getPackage_type() {return package_type;}

    public void setPackage_type(String package_type) {this.package_type = package_type;}*/

    
    
    public String getWeight() {
        return weight;
    }

    public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public PackageType getPackage_type() {
		return package_type;
	}

	public void setPackage_type(PackageType package_type) {
		this.package_type = package_type;
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
