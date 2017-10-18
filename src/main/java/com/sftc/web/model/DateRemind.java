package com.sftc.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 日期提醒类
 * @date 2017/4/25
 * @Time 上午10:39
 */
@ApiModel(value = "日期提醒类")
public class DateRemind extends Object {

    private int id;

    private String create_time;

    private String describe;

    private String date;

    private UserContact userContact;

    private int user_contact_id;
    // 所属的用户Id

    private int user_id;

    public DateRemind(String describe, String date, int user_id, int user_contact_id) {
        this.describe = describe;
        this.date = date;
        this.user_id = user_id;
        this.user_contact_id = user_contact_id;
    }

    public DateRemind() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContact userContact) {
        this.userContact = userContact;
    }

    public int getUser_contact_id() {
        return user_contact_id;
    }

    public void setUser_contact_id(int user_contact_id) {
        this.user_contact_id = user_contact_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
