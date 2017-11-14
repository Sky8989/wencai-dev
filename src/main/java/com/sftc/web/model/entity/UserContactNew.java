package com.sftc.web.model.entity;

import lombok.Getter;
import lombok.Setter;

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
    @Setter @Getter
    private int id;// 用户id

    @Setter @Getter
    private int user_id; // 好友id

    @Setter @Getter
    private int friend_id;

    private int is_tag_star;// 是否星标好友

    @Setter @Getter
    private int lntimacy; // 亲密度

    @Setter @Getter
    private String create_time; // 创建时间

    @Setter @Getter
    private String notes; // 好友备注

    @Setter @Getter
    private String picture_address;// 好友图片

    @Setter @Getter
    private String mobile; // 好友手机号

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

    public UserContactNew() {}

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

    public int getIs_tag_star() {return is_tag_star;}

    public void setIs_tag_star(int is_tag_star) {this.is_tag_star = is_tag_star;}
}
