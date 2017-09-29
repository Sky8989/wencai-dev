package com.sftc.web.dao.mybatis;

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

    // 更新好友亲密度
    void updateUserContactLntimacy(UserContactNew UserContactNew);

    // 添加好友备注
    void updateNotes(@Param("id") int id, @Param("notes") String notes);

    // 添加好友图片
    void updatePicture(@Param("id") int id, @Param("picture_address") String picture_address);

    // 添加 备注 图片 手机号
    void updateNotesPictureMobile(@Param("id") int id, @Param("notes") String notes,
                                  @Param("picture_address") String picture_address, @Param("mobile") String mobile);

    /**
     * CMS 获取好友列表 分页+条件
     */
    List<UserContactNew> selectByPage(UserContactNew userContactNew);
}
