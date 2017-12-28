package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.UserContactLabel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactLabelMapper {

    //添加标签
    void addLabel(UserContactLabel label);

    //获取好友标签列表
    List<UserContactLabel> getFriendLabelList(int user_contact_id);

    //删除好友标签
    void deleteFriendLabel(int id);
}
