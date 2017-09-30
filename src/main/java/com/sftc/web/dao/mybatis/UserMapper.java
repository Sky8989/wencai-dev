package com.sftc.web.dao.mybatis;

import com.sftc.web.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User selectUserByPhone(String user_phone);

    List<User> selectUserByOpenid(String open_id);

    User selectUserByUserId(long user_id);

    void insertOpenid(User user);

    void addMerchant(User merchant);

    User getUuidAndtoken(int order_id);

    void insertWithAvatarAndName(User user);

    void updateUserOfAvatar(User user);

    // 动态插入
    void updateUser(User user);

    /// 下面是cms系统用到的mapper
    List<User> selectByPage(User user);
}
