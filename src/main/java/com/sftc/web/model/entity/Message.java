package com.sftc.web.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "c_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private int id;

    @Setter @Getter
    private String message_type; // 消息类型 RECEIVE_EXPRESS / RECEIVE_ADDRESS，收到快递 / 收到好友地址

    @Setter @Getter
    private int is_read;// 是否已读

    @Setter @Getter
    private int express_id;// 快递编号

    @Setter @Getter
    private String user_uuid;// 用户编号

    @Setter @Getter
    private String create_time; // 创建时间

    public Message(String message_type, int is_read, int express_id, String user_uuid) {
        this.message_type = message_type;
        this.is_read = is_read;
        this.express_id = express_id;
        this.user_uuid = user_uuid;
        this.create_time = Long.toString(new Date().getTime());
    }

    public Message() {
        super();
    }
}
