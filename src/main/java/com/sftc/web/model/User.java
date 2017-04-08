package com.sftc.web.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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
@JsonIgnoreProperties("password")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

    private int id;
    // 用户姓名
    private String username;
    //用户密码
    private String password;

    private String open_id;

    private List<Order> userList;
    private List<Address> addressList;
    private List<Contact> concatList;
    private List<OpinionFeedback> opinionFeedbackList;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Contact> getConcatList() {
        return concatList;
    }

    public void setConcatList(List<Contact> concatList) {
        this.concatList = concatList;
    }

    public List<OpinionFeedback> getOpinionFeedbackList() {
        return opinionFeedbackList;
    }

    public void setOpinionFeedbackList(List<OpinionFeedback> opinionFeedbackList) {
        this.opinionFeedbackList = opinionFeedbackList;
    }
}
