package com.sftc.web.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 用户类
 * @date 17/4/1
 * @Time 下午9:00
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

    private int id;
    // 登陆用手机号
    private String user_phone;
    // 密码
    private transient String user_password;
    // 微信端保存的openid
    private String open_id;

    private List<Order> userList;
    private List<Address> addressList;
    private List<UserContact> concatList;
    private List<OpinionFeedback> opinionFeedbackList;

    public User() {}

    public User(String user_phone) {
        this.user_phone = user_phone;
    }

    public User(String user_phone, String user_password) {
        this.user_phone = user_phone;
        this.user_password = user_password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
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

    public List<Order> getUserList() {
        return userList;
    }

    public void setUserList(List<Order> userList) {
        this.userList = userList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public List<UserContact> getConcatList() {
        return concatList;
    }

    public void setConcatList(List<UserContact> concatList) {
        this.concatList = concatList;
    }

    public List<OpinionFeedback> getOpinionFeedbackList() {
        return opinionFeedbackList;
    }

    public void setOpinionFeedbackList(List<OpinionFeedback> opinionFeedbackList) {
        this.opinionFeedbackList = opinionFeedbackList;
    }
}
