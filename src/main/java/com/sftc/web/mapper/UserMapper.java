package com.sftc.web.mapper;

import com.sftc.web.model.User;


public interface UserMapper {

    public User selectUserByPhone(String user_phone);

    public User selectUserByOpenid(String open_id);

    public int insertOpenid(User user);

    void addMerchant(User merchant);

    User getUuidAndtoken(int order_id);


}
