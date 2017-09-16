package com.sftc.web.model;

import java.util.List;

public class UserContact extends Object {

    private int id;
    private int user_id;
    private int friend_id;
    // 创建时间
    private String create_time;
    // 是否标星
    private int is_tag_star;
    // 亲密度
    private int lntimacy;
    // 好友备注
    private String notes;
    // 好友图片
    private String picture_address;
    // 好友手机号
    private String mobile;

    private User friend_info;
    private List<UserContactLabel> userContactLabelList;
    private List<DateRemind> dateRemindList;

    public UserContact() {
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

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_tag_star() {
        return is_tag_star;
    }

    public void setIs_tag_star(int is_tag_star) {
        this.is_tag_star = is_tag_star;
    }

    public int getLntimacy() {
        return lntimacy;
    }

    public void setLntimacy(int lntimacy) {
        this.lntimacy = lntimacy;
    }

    public User getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(User friend_info) {
        this.friend_info = friend_info;
    }

    public List<UserContactLabel> getUserContactLabelList() {
        return userContactLabelList;
    }

    public void setUserContactLabelList(List<UserContactLabel> userContactLabelList) {
        this.userContactLabelList = userContactLabelList;
    }

    public List<DateRemind> getDateRemindList() {
        return dateRemindList;
    }

    public void setDateRemindList(List<DateRemind> dateRemindList) {
        this.dateRemindList = dateRemindList;
    }

    public String getNotes() {return notes;}

    public void setNotes(String notes) {this.notes = notes;}

    public String getPicture_address() {return picture_address;}

    public void setPicture_address(String picture_address) {this.picture_address = picture_address;}

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}
}
