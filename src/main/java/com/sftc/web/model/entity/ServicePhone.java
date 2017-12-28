package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@ApiModel(value = "服务电话")
@Entity
@Table(name = "c_service_phone")
public class ServicePhone{

 	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @ApiModelProperty(name="主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty(name="城市",example="深圳",required = true)
    @Setter @Getter
    private String city;

    @ApiModelProperty(name="服务电话",required = true)
    @Setter @Getter
    private String phone;

    @ApiModelProperty(name="创建时间",hidden=true)
    @Setter @Getter
    private String create_time;

    @ApiModelProperty(name="修改时间",hidden=true)
    @Setter @Getter
    private String update_time;

    public ServicePhone(){}
}
