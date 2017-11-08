package com.sftc.web.mapper;

import com.sftc.web.model.User;
import org.apache.ibatis.annotations.Param;


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

    // 动态插入
    void updateUser(User user);

    //     <!--下面是cms系统用到的mapper-->
    List<User> selectByPage(User user);
}
