package com.sftc.web.mapper;

import com.sftc.web.model.Paging;
import com.sftc.web.model.UserContact;
import com.sftc.web.model.UserContactNew;
import com.sftc.web.model.apiCallback.ContactCallback;
import com.sftc.web.model.reqeustParam.UserContactParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserContactMapper {

    // 好友列表
    List<UserContact> friendList(Paging paging);

    int selectFriendCount(int user_id);

    // 添加好友
    void addFriend(UserContactParam userContactParam);

    void updateFriend(UserContact userContact);

    UserContact friendDetail(@Param("user_id") int user_id, @Param("friend_id") int friend);

    List<ContactCallback> selectCirclesContact(UserContactParam userContactParam);

    UserContactNew selectByUserIdAndShipId(UserContactNew userContactNew);

    void insertUserContact(UserContactNew userContactNew);

    /**
     * 星标好友
     */
    void starFriend(@Param("user_id") int user_id, @Param("friend_id") int friend_id, @Param("is_tag_star") int is_star);
}
