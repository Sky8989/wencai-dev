package com.sftc.web.model;

public class UserContactLabel extends Object {
    private int id;
    //联系人id
    private int user_contact_id;
    //标签
    private String label;
    //创建时间
    private String create_time;

    public UserContactLabel(int user_contact_id, String label, String create_time) {
        this.create_time = create_time;
        this.user_contact_id = user_contact_id;
        this.label = label;
    }

    public UserContactLabel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_contact_id() {
        return user_contact_id;
    }

    public void setUser_contact_id(int user_contact_id) {
        this.user_contact_id = user_contact_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
