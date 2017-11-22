package com.sftc.web.model.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sftc.web.model.others.Object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sftc_system_label")
@ApiModel(value = "系统标签 新增不用传id，修改时传id")
public class SystemLabel extends Object{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name="主键",required=false)
    @Setter @Getter
    private int id;
    
	@ApiModelProperty(name="系统标签内容")
    @Setter @Getter
    private String system_label;
	
	
	@ApiModelProperty(name="创建时间",hidden=true)
	@Setter @Getter
	private String create_time;
	
	@ApiModelProperty(name="修改时间",hidden=true)
	@Setter @Getter
	private String update_time;

    public SystemLabel(){}
}
