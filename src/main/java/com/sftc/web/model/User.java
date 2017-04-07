package com.sftc.web.model;

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
public class User {

    private int id;
    // 用户姓名
    private String name;
    //用户密码
    private String password;

    private String openId;

    private List<Order> userList;
    private List<Address> addressList;
    private List<Contact> concatList;
    private List<OpinionFeedback> opinionFeedbackList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
