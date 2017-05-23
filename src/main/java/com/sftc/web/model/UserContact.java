package com.sftc.web.model;

import java.util.List;

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
    // 创建时间
    private String create_time;
    // 好友姓名
    private String name;
    //好友电话
    private String phone;
    // 用户头像
    private String icon;
    // 是否标星
    private int is_tag_star;
    // 用户微信
    private String wechat;
    // 备注
    private String remark;
    //亲密度
    private int lntimacy;

    private User merchant;
    private List<UserContactLabel> userContactLabelList;
    private List<DateRemind> dateRemindList;

    private int user_id;
    private int friend_id;

    public UserContact() {}

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

    public int getLntimacy() {
        return lntimacy;
    }

    public void setLntimacy(int lntimacy) {
        this.lntimacy = lntimacy;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIs_tag_star() {
        return is_tag_star;
    }

    public void setIs_tag_star(int is_tag_star) {
        this.is_tag_star = is_tag_star;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getMerchant() {

        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public List<UserContactLabel> getUserContactLabelList() {
        return userContactLabelList;
    }

    public void setUserContactLabelList(List<UserContactLabel> userContactLabelList) {
        this.userContactLabelList = userContactLabelList;
    }

    public List<DateRemind> getDateRemindList() {
        return dateRemindList;
    }

    public void setDateRemindList(List<DateRemind> dateRemindList) {
        this.dateRemindList = dateRemindList;
    }
}
