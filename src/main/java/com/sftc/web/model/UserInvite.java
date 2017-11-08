package com.sftc.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "sftc_user_invite")
@ApiModel(value = "用户邀请")
public class UserInvite extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private int id;

    @ApiModelProperty(value = "用户id")
    private int user_id;

    @ApiModelProperty(value = "邀请渠道")
    private String invite_channel;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "邀请码")
    private String invite_code;

    @ApiModelProperty("创建时间")
    private String create_time;

    public UserInvite() {
        super();
    }

    @Override
    public String toString() {
        return "UserInvite{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", invite_channel=" + invite_channel +
                ", city='" + city + '\'' +
                ", invite_code='" + invite_code + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getInvite_channel() {
        return invite_channel;
    }

    public void setInvite_channel(String invite_channel) {
        this.invite_channel = invite_channel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

