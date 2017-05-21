package com.sftc.web.mapper;

import com.sftc.web.model.UserContactLabel;

import java.util.List;

public interface UserContactLabelMapper {

        //添加标签
        void addLabel(UserContactLabel label);
        //获取好友标签列表
        List<UserContactLabel> getFriendLabelList(int user_contact_id);
        //删除好友标签
        void deleteFriendLabel(int id);
}
