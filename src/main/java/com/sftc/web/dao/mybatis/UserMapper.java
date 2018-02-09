package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User selectUserByPhone(String user_phone);

    List<User> selectUserByOpenid(String open_id);

    List<User> findUserByMobile(String mobile);

    User selectUserByUserId(int user_id);

    void insertOpenid(User user);

    void addMerchant(User merchant);

    User getUuidAndtoken(String order_id);

    void insertWithAvatarAndName(User user);

    void updateUserOfAvatar(User user);

    // 动态插入
    void updateUser(User user);

    /// 下面是cms系统用到的mapper
    List<User> selectByPage(User user);
}
