package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserContact extends Object {

    @Setter @Getter
    private int id;

    @Setter @Getter
    private int user_id;

    @Setter @Getter
    private int friend_id;

    @Setter @Getter
    private String create_time;// 创建时间

    private int is_tag_star;// 是否标星

    @Setter @Getter
    private int lntimacy; // 亲密度

    private String notes; // 好友备注

    @Setter @Getter
    private String picture_address;// 好友图片

    @Setter @Getter
    private String mobile;// 好友手机号

    @Setter @Getter
    private User friend_info;

    @Setter @Getter
    private List<UserContactLabel> userContactLabelList;

    @Setter @Getter
    private List<DateRemind> dateRemindList;

    public UserContact() {}

    public int getIs_tag_star() {return is_tag_star;}

    public void setIs_tag_star(int is_tag_star) {this.is_tag_star = is_tag_star;}
}
