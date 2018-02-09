package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sftc_address_history")
@ApiModel(value = "历史地址")
public class AddressHistory extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty("用户id")
    @Setter @Getter
    private int user_id;

    @ApiModelProperty("地址id")
    @Setter @Getter
    private int address_id;

    @ApiModelProperty("是否删除")
    @Setter @Getter
    private int is_delete;

    @ApiModelProperty("是否神秘[废弃]")
    @Setter @Getter
    private int is_mystery;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    public AddressHistory(){}
}
