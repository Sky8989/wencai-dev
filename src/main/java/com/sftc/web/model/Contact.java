package com.sftc.web.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 联系人类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Contact {

    private int id;
    // 当前使用者id
    private User currentUserId;
    // 使用者的好友id
    private User userFriendId;
    private List<User> userList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(User currentUserId) {
        this.currentUserId = currentUserId;
    }

    public User getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(User userFriendId) {
        this.userFriendId = userFriendId;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
