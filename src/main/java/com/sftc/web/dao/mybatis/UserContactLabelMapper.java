package com.sftc.web.dao.mybatis;

import com.sftc.web.model.UserContactLabel;

import java.util.List;

public interface UserContactLabelMapper {

    //添加标签
    void addLabel(UserContactLabel label);

    //获取好友标签列表
    List<UserContactLabel> getFriendLabelList(int id);

    //删除好友标签
    void deleteFriendLabel(int id);
}
