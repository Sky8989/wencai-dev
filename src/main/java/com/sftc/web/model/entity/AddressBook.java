package com.sftc.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sftc.web.enumeration.address.AddressBookType;
import com.sftc.web.enumeration.address.AddressType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址关系总映射
 * Created by huxingyue on 2017/7/26.
 */
@Entity
@Table(name = "sftc_address_book")
@ApiModel(value = "地址簿")
public class AddressBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    @Setter @Getter
    private Long id;

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
//
//    @ApiModelProperty("地址类型 address_history/address_book")
//    @Setter @Getter
//    private String address_type;
//
//    @ApiModelProperty("地址簿类型 sender/ship")
//    @Setter @Getter
//    private String address_book_type;
    
    @ApiModelProperty("地址类型 address_history/address_book")
    @Setter @Getter
    private AddressType address_type;

    @ApiModelProperty("地址簿类型 sender/ship")
    @Setter @Getter
    private AddressBookType address_book_type;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    public AddressBook(){}

    public AddressBook(int user_id, int address_id, int is_delete, int is_mystery, AddressType address_type, AddressBookType address_book_type, String create_time) {
        this.user_id = user_id;
        this.address_id = address_id;
        this.is_delete = is_delete;
        this.is_mystery = is_mystery;
        this.address_type = address_type;
        this.address_book_type = address_book_type;
        this.create_time = create_time;
    }
    

}

