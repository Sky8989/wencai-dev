package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 联系人类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class UserContact {

    private int id;
    // 好友姓名
    private String name;
    //好友电话
    private String phone;
    // 用户头像
    private String icon;
    // 用户邮箱
    private String email;
    // 用户微信
    private String wechat;
    // 用户生日
    private String gmt_birthday;
    // 纪念日
    private String anniversaries;
    // 公司
    private String company;
    // 部门
    private String department;
    // 职位
    private String job;
    // 备注
    private String remark;
    // 个人标签
    private String label;
    // 所在地
    private String location;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGmt_birthday() {
        return gmt_birthday;
    }

    public void setGmt_birthday(String gmt_birthday) {
        this.gmt_birthday = gmt_birthday;
    }

    public String getAnniversaries() {
        return anniversaries;
    }

    public void setAnniversaries(String anniversaries) {
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
