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
    // 创建时间
    private String create_time;
    // 登陆用手机号
    private String user_phone;
    // 密码
    private transient String user_password;
    // 微信端保存的openid
    private String open_id;

    //用户头像
    private String head_portrait;
    private List<com.sftc.web.model.Order> userList;
    private List<Address> addressList;
    private List<com.sftc.web.model.UserContact> concatList;
    private List<com.sftc.web.model.OpinionFeedback> opinionFeedbackList;

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public List<com.sftc.web.model.Order> getUserList() {
        return userList;
    }

    public void setUserList(List<com.sftc.web.model.Order> userList) {
        this.userList = userList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public List<com.sftc.web.model.UserContact> getConcatList() {
        return concatList;
    }

    public void setConcatList(List<com.sftc.web.model.UserContact> concatList) {
        this.concatList = concatList;
    }

    public List<com.sftc.web.model.OpinionFeedback> getOpinionFeedbackList() {
        return opinionFeedbackList;
    }

    public void setOpinionFeedbackList(List<com.sftc.web.model.OpinionFeedback> opinionFeedbackList) {
        this.opinionFeedbackList = opinionFeedbackList;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }
}
