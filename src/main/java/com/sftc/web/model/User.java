package com.sftc.web.model;

import java.sql.Date;
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
    // 用户头像
    private String icon;
    // 用户邮箱
    private String email;
    // 用户微信
    private String wechat;
    // 用户生日
    private Date birthday;
    // 纪念日
    private Date anniversaries;
    // 公司
    private String company;
    // 部门
    private String department;
    // 职位
    private String position;
    // 备注
    private String remark;
    // 个人标签
    private String label;
    // 所在地
    private String location;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getAnniversaries() {
        return anniversaries;
    }

    public void setAnniversaries(Date anniversaries) {
        this.anniversaries = anniversaries;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
