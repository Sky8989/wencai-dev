package com.sftc.web.model;

/**
 * Created by huxingyue on 2017/6/27.
 * 对应  sftc_message 消息通知表
 */
public class Message {
    private int id;
    //消息类型 RECEIVE_EXPRESS / RECEIVE_ADDRESS，收到快递 / 收到好友地址
    private String message_type;
    //是否已读
    private int is_read;
    //快递编号
    private int express_id;
    //用户编号
    private int user_id;
    //创建时间
    private String create_time;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getMessage_type() {return message_type;}

    public void setMessage_type(String message_type) {this.message_type = message_type;}

    public int getIs_read() {return is_read;}

    public void setIs_read(int is_read) {this.is_read = is_read;}

    public int getExpress_id() {return express_id;}

    public void setExpress_id(int express_id) {this.express_id = express_id;}

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public String getCreate_time() {return create_time;}

    public void setCreate_time(String create_time) {this.create_time = create_time;}
}
