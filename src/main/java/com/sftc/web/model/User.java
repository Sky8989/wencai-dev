package com.sftc.web.model;

public class User extends Object {
    private int id;
    private String uuid;
    //用户名字
    private String name;
    //用户手机
    private String mobile;
    //用户头像
    private String avatar;
    //用户密码
    private String user_password;
    private String open_id;
    private String create_time;
    private String js_code;
    private Object summary;
    private Object attributes;
    private Object tags;

    private Token token;


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }


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

    public User() {
    }

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Object getSummary() {
        return summary;
    }

    public void setSummary(Object summary) {
        this.summary = summary;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
