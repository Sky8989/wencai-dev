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
    // 是否标星
    private int is_tag_star;
    // 用户微信
    private String wechat;
    // 备注
    private String remark;
    //亲密度
    private int lntimacy;
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLntimacy() {
        return lntimacy;
    }

    public void setLntimacy(int lntimacy) {
        this.lntimacy = lntimacy;
    }
}
