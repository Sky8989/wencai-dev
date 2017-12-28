package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "c_compensate")
@ApiModel(value = "赔偿规则 ")
public class Compensate extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name="主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty(name="时间",hidden=true)
    @Setter @Getter
    private String create_time;

    @ApiModelProperty(name="标题",example = "问题标题",required = true)
    @Setter @Getter
    private String title;

    @ApiModelProperty(name="内容",example = "问题答案",required = true)
    @Setter @Getter
    private String contents;

}
