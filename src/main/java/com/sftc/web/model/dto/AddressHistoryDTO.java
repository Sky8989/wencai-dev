package com.sftc.web.model.dto;

import com.sftc.web.model.Object;
import com.sftc.web.model.entity.Address;

public class AddressHistoryDTO extends Object {

    private int id;
    // 用户编号
    private int user_id;
    // 地址编号
    private transient Long address_id;
    // 是否删除
    private transient int is_delete;
    // 是否神秘件
    private int is_mystery;
    // 创建时间
    private String create_time;
    // 好友微信名
    private String ship_wechatname;
    // 地址
    private Address address;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public Long getAddress_id() {return address_id;}

    public void setAddress_id(Long address_id) {this.address_id = address_id;}

    public int getIs_delete() {return is_delete;}

    public void setIs_delete(int is_delete) {this.is_delete = is_delete;}

    public int getIs_mystery() {return is_mystery;}

    public void setIs_mystery(int is_mystery) {this.is_mystery = is_mystery;}

    public String getCreate_time() {return create_time;}

    public void setCreate_time(String create_time) {this.create_time = create_time;}

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

    public String getShip_wechatname() {return ship_wechatname;}

    public void setShip_wechatname(String ship_wechatname) {this.ship_wechatname = ship_wechatname;}
}
