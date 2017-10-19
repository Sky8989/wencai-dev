package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

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
    private Long id;

    @ApiModelProperty("用户id")
    private int user_id;

    @ApiModelProperty("地址id")
    private int address_id;

    @ApiModelProperty("是否删除")
    private int is_delete;

    @ApiModelProperty("是否神秘[废弃]")
    private int is_mystery;

    @ApiModelProperty("地址类型 address_history/address_book")
    private String address_type;

    @ApiModelProperty("地址簿类型 sender/ship")
    private String address_book_type;

    @ApiModelProperty("创建时间")
    private String create_time;

    public AddressBook() {
        super();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public int getAddress_id() {return address_id;}

    public void setAddress_id(int address_id) {this.address_id = address_id;}

    public int getIs_delete() {return is_delete;}

    public void setIs_delete(int is_delete) {this.is_delete = is_delete;}

    public int getIs_mystery() {return is_mystery;}

    public void setIs_mystery(int is_mystery) {this.is_mystery = is_mystery;}

    public String getAddress_type() {return address_type;}

    public void setAddress_type(String address_type) {this.address_type = address_type;}

    public String getAddress_book_type() {return address_book_type;}

    public void setAddress_book_type(String address_book_type) {this.address_book_type = address_book_type;}

    public String getCreate_time() {return create_time;}

    public void setCreate_time(String create_time) {this.create_time = create_time;}

    public AddressBook(int user_id, int address_id, int is_delete, int is_mystery, String address_type, String address_book_type, String create_time) {
        this.user_id = user_id;
        this.address_id = address_id;
        this.is_delete = is_delete;
        this.is_mystery = is_mystery;
        this.address_type = address_type;
        this.address_book_type = address_book_type;
        this.create_time = create_time;
    }

}
