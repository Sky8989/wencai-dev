package com.sftc.web.mapper;

import com.sftc.web.model.User;

public interface UserMapper {

    public User findUserById(String phone);
}
