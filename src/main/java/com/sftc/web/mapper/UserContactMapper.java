package com.sftc.web.mapper;

import com.sftc.web.model.UserContact;

import java.util.List;
import java.util.Map;

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
    List<UserContact> friendList(Map<String,Integer> params);
    void addFriend(UserContact userContact);
    void updateFriend(UserContact userContact);
    UserContact friendDetail(int id);
}
