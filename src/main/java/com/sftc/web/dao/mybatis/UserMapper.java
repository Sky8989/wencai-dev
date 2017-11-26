package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User selectUserByPhone(String user_phone);

    List<User> selectUserByOpenid(String open_id);

    User selectUserByUserId(int user_id);

    void insertOpenid(User user);

    User getUuidAndtoken(String order_id);

    void insertWithAvatarAndName(User user);

    void updateUserOfAvatar(User user);

    // 动态插入
    void updateUser(User user);
}
