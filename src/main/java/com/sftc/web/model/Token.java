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
    // 创建时间
    private String create_time;
    private String token;
    // 是否已经注销
    private int is_logout;
    // 创建时间
    private String create_at;
    // 更新时间
    private String expires_in ;
    private String gmt_modified;
    // 到期时间
    private String gmt_expiry;
    // 属于哪个用户
    private String access_token;

    private String refresh_token;

    private String uuid;
    private int user_id;

    public Token() {
    }

    public Token(String create_time, int is_logout, String gmt_modified, String gmt_expiry, int user_id) {
        this.create_time = create_time;
        this.is_logout = is_logout;
        this.gmt_modified = gmt_modified;
        this.gmt_expiry = gmt_expiry;
        this.user_id = user_id;
    }

    public Token(String create_time, String create_at, String gmt_modified, String gmt_expiry,
                 String access_token, String refresh_token, int user_id, String uuid) {
        this.create_time = create_time;
        this.is_logout = is_logout;
        this.create_at = create_at;
        this.gmt_modified = gmt_modified;
        this.gmt_expiry = gmt_expiry;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.user_id = user_id;
        this.uuid = uuid;
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

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
