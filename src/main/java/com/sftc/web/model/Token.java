package com.sftc.web.model;

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
public class Token {

    private int id;
    private String token;
    // 是否已经注销
    private int is_logout;
    // 创建时间
    private String gmt_create;
    // 更新时间
    private String gmt_modified;
    // 到期时间
    private String gmt_expiry;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIs_logout() {
        return is_logout;
    }

    public void setIs_logout(int is_logout) {
        this.is_logout = is_logout;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(String gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    public String getGmt_expiry() {
        return gmt_expiry;
    }

    public void setGmt_expiry(String gmt_expiry) {
        this.gmt_expiry = gmt_expiry;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
