package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

public class User extends Object {

    @Setter @Getter
    private String uuid;

    @Setter @Getter
    private String name;//用户名字

    @Setter @Getter
    private String mobile;//用户手机

    @Setter @Getter
    private String avatar;//用户头像

    @Setter @Getter
    private String email;//用户邮箱

    @Setter @Getter
    private String type;//用户类型

    @Setter @Getter
    private String status;//状态

    @Setter @Getter
    private String status_code;//记录是否被禁用或删除

    @Setter @Getter
    private String account_type;//账户类型

    @Setter @Getter
    private String password;//用户密码

    @Setter @Getter
    private String create_at;

    @Setter @Getter
    private String js_code;

    @Setter @Getter
    private Object summary;

    @Setter @Getter
    private String attributes;

    @Setter @Getter
    private Token token;

    @Setter @Getter
    private Integer pageNumKey; //分页参数

    @Setter @Getter
    private Integer pageSizeKey;

    private String openId;

    public User(String password, String attributes, String createAt) {
        this.password = password;
        this.attributes = attributes;
        this.create_at = createAt;
    }

    public String getOpenId() {
        JSONObject jsonObject = JSONObject.fromObject(attributes);
        String openId = null;
        if(jsonObject.containsKey("c_wxopenid")) {
            openId = jsonObject.getString("c_wxopenid");
        }
        return openId;
    }

    public void setOpenId(String attributes) {
        JSONObject jsonObject = JSONObject.fromObject(attributes);
        String openId = null;
        if(jsonObject.containsKey("c_wxopenid")) {
            openId = jsonObject.getString("c_wxopenid");
        }
        this.openId = openId;
    }

    public User(String uuid, String name, String mobile, String avatar, String createAt) {
        this.uuid = uuid;
        this.name = name;
        this.mobile = mobile;
        this.avatar = avatar;

        this.create_at = createAt;
    }

    public User() {}
}
