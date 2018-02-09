package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: token表
 * @date 2017/4/12
 * @Time 上午12:30
 */
public class Token extends Object {

    @Setter @Getter
    private int id;

    @Setter @Getter
    private String create_time; // 创建时间

    @Setter @Getter
    private String token;

    private int is_logout;// 是否已经注销

    @Setter @Getter
    private String expires_in;// 更新时间

    @Setter @Getter
    private String gmt_modified;

    @Setter @Getter
    private String gmt_expiry;// 到期时间

    @Setter @Getter
    private String local_token; // 属于哪个用户

    @Setter @Getter
    private String access_token;

    @Setter @Getter
    private String refresh_token;

    @Setter @Getter
    private int user_id;

    public Token() {}

    /**
     * CMS使用的构造方法
     *
     * @param request
     */
    public Token(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("user_id") != null && !"".equals(request.getParameter("user_id"))) {
            this.user_id = Integer.parseInt(request.getParameter("user_id"));
        }
    }

    public Token(int user_id, String token) {
        this.user_id = user_id;
        this.local_token = token;
        this.create_time = Long.toString(System.currentTimeMillis());
        this.gmt_modified = Long.toString(System.currentTimeMillis());
        this.gmt_expiry = (Long.parseLong(gmt_modified) + 2592000000L) + "";
    }

    public Token(String create_time, int is_logout, String gmt_modified, String gmt_expiry, int user_id) {
        this.create_time = create_time;
        this.is_logout = is_logout;
        this.gmt_modified = gmt_modified;
        this.gmt_expiry = gmt_expiry;
        this.user_id = user_id;
    }


    public Token(String create_time,int is_logout, String gmt_modified, String gmt_expiry,
                 String access_token, String refresh_token, int user_id) {
        this.create_time = create_time;
        this.is_logout = is_logout;
        this.gmt_modified = gmt_modified;
        this.gmt_expiry = gmt_expiry;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.user_id = user_id;
    }

    public int getIs_logout() {return is_logout;}

    public void setIs_logout(int is_logout) {this.is_logout = is_logout;}
}
