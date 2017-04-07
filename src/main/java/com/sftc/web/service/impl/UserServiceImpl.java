package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.User;
import com.sftc.web.service.UserService;

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
public class UserServiceImpl extends AbstractBasicService implements UserService {

    public APIResponse login(User users) {
        String password = users.getPassword();
        users = userMapper.selectUserByLogin(users);
        if (users == null) {
            status = APIStatus.USER_NOT_EXIST;
        } else {
            if (!password.equals(users.getPassword())) {
                status = APIStatus.USER_FAIL;
            }
        }
        return APIUtil.getResponse(status.getState(), status.getMessage(),
                status.equals(APIStatus.SUCCESS) ? users : null);
    }
}
