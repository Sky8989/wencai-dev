package com.sftc.web.mapper;

import com.sftc.web.model.User;


public interface UserMapper {

    User selectUserByPhone(String user_phone);

    User selectUserByOpenid(String open_id);

    User selectUserByUserId(int user_id);

    int insertOpenid(User user);

    void addMerchant(User merchant);

    User getUuidAndtoken(int order_id);
    int insertWithAvatarAndName(User user);

}
