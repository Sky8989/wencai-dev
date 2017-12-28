package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "c_user_invite")
@ApiModel(value = "用户邀请")
public class UserInvite extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty(value = "用户uuid")
    @Setter @Getter
    private String user_uuid;

    @ApiModelProperty(value = "邀请渠道")
    @Setter @Getter
    private String invite_channel;

    @ApiModelProperty(value = "城市")
    @Setter @Getter
    private String city;

    @ApiModelProperty(value = "邀请码")
    @Setter @Getter
    private String invite_code;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    public UserInvite() {
        super();
    }

    @Override
    public String toString() {
        return "UserInvite{" +
                "id=" + id +
                ", user_uuid=" + user_uuid +
                ", invite_channel=" + invite_channel +
                ", city='" + city + '\'' +
                ", invite_code='" + invite_code + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}

