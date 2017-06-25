package com.sftc.web.mapper;

import com.sftc.web.model.Paging;
import com.sftc.web.model.UserContact;
import com.sftc.web.model.apiCallback.ContactCallback;
import com.sftc.web.model.reqeustParam.UserContactParam;

import java.util.List;


public interface UserContactMapper {

    // 好友列表
    List<UserContact> friendList(Paging paging);

    int selectFriendCount(int user_id);

    // 添加好友
    void addFriend(UserContactParam userContactParam);

    void updateFriend(UserContact userContact);

    UserContact friendDetail(int id);

    List<ContactCallback> selectCirclesContact(UserContactParam userContactParam);
}
