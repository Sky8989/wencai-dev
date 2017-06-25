package com.sftc.web.mapper;

import com.sftc.web.model.User;


import java.util.List;


public interface UserMapper {

    User selectUserByPhone(String user_phone);

    List<User> selectUserByOpenid(String open_id);


    User selectUserByUserId(int user_id);

    int insertOpenid(User user);

    void addMerchant(User merchant);

    User getUuidAndtoken(int order_id);

    int insertWithAvatarAndName(User user);

    void updateUserOfAvatar(User user);
}
