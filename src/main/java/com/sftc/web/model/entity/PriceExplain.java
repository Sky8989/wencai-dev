package com.sftc.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel(value = "价格说明")
@Entity
@Table(name = "sftc_price_explain")
public class PriceExplain extends com.sftc.web.model.others.Object{
	
 	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name="主键")
    @Setter @Getter
    private int id;
	
    @ApiModelProperty(name="城市",example="深圳",required = true)
    @Setter @Getter
    private String city;

    /**距离定价*/
    @ApiModelProperty(name="距离定价",required = true)
    @Setter @Getter
    private String distance_price;

    /**重量价格*/
    @ApiModelProperty(name="重量价格",required = true)
    @Setter @Getter
    private String weight_price;
    
    @ApiModelProperty(name="创建时间",hidden=true)
    @Setter @Getter
    private String create_time;
    
    @ApiModelProperty(name="修改时间",hidden=true)
    @Setter @Getter
    private String update_time;

    public PriceExplain(){}
}
