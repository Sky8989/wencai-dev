package com.sftc.web.model.reqeustParam;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.reqeustParam
 * @Description:
 * @date 2017/5/27
 * @Time 上午10:10
 */
public class UserParam {

    private int id;
    private String token;
    private String js_code;
    //头像和昵称

    private String avatar;
    private String name;
    public  String getAvatar() {
        return avatar;
    }
    public void   setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getName() {
        return name;
    }
    public void   setName(String name) {
        this.name = name;
    }

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

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }
}
