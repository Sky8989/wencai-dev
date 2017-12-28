package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 地址关系总映射
 * Created by huxingyue on 2017/7/26.
 */
@Entity
@Table(name = "c_address_book")
@ApiModel(value = "地址簿")
public class AddressBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    @Setter @Getter
    private Long id;

    @ApiModelProperty("用户uuid")
    @Setter @Getter
    private String user_uuid;

    @ApiModelProperty("地址id")
    @Setter @Getter
    private Integer address_id;

    @ApiModelProperty("是否删除")
    @Setter @Getter
    private Integer is_delete;

    @ApiModelProperty("是否神秘[废弃]")
    @Setter @Getter
    private Integer is_mystery;

    @ApiModelProperty("地址类型 address_history/address_book")
    @Setter @Getter
    private String address_type;

    @ApiModelProperty("地址簿类型 sender/ship")
    @Setter @Getter
    private String address_book_type;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    public AddressBook(){}

    public AddressBook(String user_uuid, Integer address_id, Integer is_delete, Integer is_mystery, String address_type, String address_book_type, String create_time) {
        this.user_uuid = user_uuid;
        this.address_id = address_id;
        this.is_delete = is_delete;
        this.is_mystery = is_mystery;
        this.address_type = address_type;
        this.address_book_type = address_book_type;
        this.create_time = create_time;
    }

}

