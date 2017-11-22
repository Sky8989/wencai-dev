package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

import static com.sftc.tools.constant.DKConstant.DK_USER_AVATAR_DEFAULT;

public class User extends Object {
    @Setter @Getter
    private int id;

    @Setter @Getter
    private int invite_id;//邀请表id

    @Setter @Getter
    private String uuid;

    @Setter @Getter
    private String name;//用户名字

    @Setter @Getter
    private String mobile;//用户手机

    @Setter @Getter
    private String avatar;//用户头像

    @Setter @Getter
    private String user_password;//用户密码

    @Setter @Getter
    private String open_id;

    @Setter @Getter
    private String chanel;//用户渠道

    @Setter @Getter
    private String unionid;//用户唯一标识unionid

    @Setter @Getter
    private String session_key;

    @Setter @Getter
    private String create_time;

    @Setter @Getter
    private String js_code;

    @Setter @Getter
    private Object summary;

    @Setter @Getter
    private Object attributes;

    @Setter @Getter
    private Object tags;

    @Setter @Getter
    private Token token;

    @Setter @Getter
    private Integer pageNumKey; //分页参数

    @Setter @Getter
    private Integer pageSizeKey;

    public User(String user_password, String open_id, String create_time) {
        this.user_password = user_password;
        this.open_id = open_id;
        this.create_time = create_time;
    }


    public User(String uuid, String name, String mobile, String avatar, String create_time) {
        this.uuid = uuid;
        this.name = name;
        this.mobile = mobile;
        this.avatar = avatar;

        this.create_time = create_time;
    }

    public User() {}

    /**
     * 基于HttpServletRequest作为参数的构造方法 用于cms
     * 后期便于应用扩展工厂模式 将此参数抽出
     */
    public User(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id")))
        {this.id = Integer.parseInt(request.getParameter("id"));}
        if (request.getParameter("name") != null && !"".equals(request.getParameter("name")))
        {this.name = request.getParameter("name");}
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", invite_id=" + invite_id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", user_password='" + user_password + '\'' +
                ", open_id='" + open_id + '\'' +
                ", session_key='" + session_key + '\'' +
                ", create_time='" + create_time + '\'' +
                ", js_code='" + js_code + '\'' +
                ", summary=" + summary +
                ", attributes=" + attributes +
                ", tags=" + tags +
                ", token=" + token +
                ", pageNumKey=" + pageNumKey +
                ", pageSizeKey=" + pageSizeKey +
                '}';
    }
}
