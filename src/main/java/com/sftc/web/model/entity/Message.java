package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sftc_message")
@ApiModel(value = "消息通知实体类")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    @ApiModelProperty(name = "id",value = "消息id",dataType = "int")
    private int id;

    @Setter @Getter
    @ApiModelProperty(name = "message_type",value = "消息类型",example = "RECEIVE_EXPRESS")
    private String message_type; // 消息类型 RECEIVE_EXPRESS / RECEIVE_ADDRESS，收到快递 / 收到好友地址

    @Setter @Getter
    @ApiModelProperty(name = "is_read",value = "是否已读",example = "0",dataType = "int")
    private int is_read;// 是否已读

    @Setter @Getter
    @ApiModelProperty(name = "express_id",value = "快递编号",dataType = "int")
    private int express_id;// 快递编号

    @Setter @Getter
    @ApiModelProperty(name = "user_id",value = "用户编号",dataType = "int")
    private int user_id;// 用户编号

    @Setter @Getter
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private String create_time; // 创建时间

    public Message(String message_type, int is_read, int express_id, int user_id) {
        this.message_type = message_type;
        this.is_read = is_read;
        this.express_id = express_id;
        this.user_id = user_id;
        this.create_time = Long.toString(new Date().getTime());
    }

    public Message() {
        super();
    }
}
