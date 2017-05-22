package com.sftc.web.mapper;

import com.sftc.web.model.UserContact;
import com.sftc.web.model.reqeustParam.UserContactParam;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.mapper
 * @Description:
 * @date 2017/4/7
 * @Time 上午8:44
 */
public interface UserContactMapper {

    // 好友列表
    List<UserContact> friendList(int user_id);

    // 添加好友
    void addFriend(UserContactParam userContactParam);

    void updateFriend(UserContact userContact);

    UserContact friendDetail(int id);
}
