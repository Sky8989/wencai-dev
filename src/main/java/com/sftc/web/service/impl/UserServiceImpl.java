package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.User;
import com.sftc.web.service.UserService;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 用户操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
// @Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public APIResponse login(String phone) {
        APIStatus status = APIStatus.SUCCESS;
        User user = userMapper.findUserById(phone);
        System.out.println(user.getName());
        if (user == null)
            status = APIStatus.USER_NOT_EXIST;
        // } else {
        //     status = APIStatus.USER_FAIL;
        // }
        return APIUtil.getResponse(status.getState(), status.getMessage(),
                status.equals(APIStatus.SUCCESS) ? user : null);
    }
}
