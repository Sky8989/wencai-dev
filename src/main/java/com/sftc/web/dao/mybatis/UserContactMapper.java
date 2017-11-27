package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.FriendRecordDTO;
import com.sftc.web.model.dto.WxNameDTO;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.entity.UserContact;
import com.sftc.web.model.entity.UserContactNew;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactMapper {

    // 好友列表
    List<UserContact> friendList(FriendListVO friendListVO);

    UserContact friendDetail(@Param("user_id") int user_id, @Param("friend_id") int friend);

    List<FriendRecordDTO> selectCirclesContact(UserContactParamVO userContactParamVO);

    UserContactNew selectByUserIdAndShipId(UserContactNew userContactNew);

    void insertUserContact(UserContactNew userContactNew);

    /**
     * 星标好友
     */
    void starFriend(@Param("user_id") int user_id, @Param("friend_id") int friend_id, @Param("is_tag_star") int is_star);

    // 更新好友亲密度
    void updateUserContactLntimacy(UserContactNew UserContactNew);

    // 添加 备注 图片 手机号
    void updateNotesPictureMobile(@Param("id") int id, @Param("notes") String notes,
                                  @Param("picture_address") String picture_address, @Param("mobile") String mobile);
}
