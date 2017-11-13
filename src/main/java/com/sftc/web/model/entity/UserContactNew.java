package com.sftc.web.model.entity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/6/25.
 */
public class UserContactNew {
    /**
     * @Author:hxy starmoon1994
     * @Description: 新建立的用户关系实体，与旧有
     * 一条记录一条用户关系 这是一种单向关系
     * @Date:16:32 2017/6/25
     */
    private int id;
    // 用户id
    private int user_id;
    // 好友id
    private int friend_id;
    // 是否星标好友
    private int is_tag_star;
    // 亲密度
    private int lntimacy;
    // 创建时间
    private String create_time;
    // 好友备注
    private String notes;
    // 好友图片
    private String picture_address;
    // 好友手机号
    private String mobile;

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNotes() {return notes;}

    public void setNotes(String notes) {this.notes = notes;}

    public String getPicture_address() {return picture_address;}

    public void setPicture_address(String picture_address) {this.picture_address = picture_address;}

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserContactNew{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", friend_id=" + friend_id +
                ", is_tag_star=" + is_tag_star +
                ", lntimacy=" + lntimacy +
                ", create_time='" + create_time + '\'' +
                '}';
    }

    public UserContactNew() {
    }

    /**
     * 基于HttpServletRequest作为参数的构造方法 用于cms
     * 后期便于应用扩展工厂模式 将此参数抽出
     */
    public UserContactNew(HttpServletRequest request) {
        if (request.getParameter("user_id") != null && !"".equals(request.getParameter("user_id"))) {
            this.user_id = Integer.parseInt(request.getParameter("user_id"));
        }
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
    }
}
