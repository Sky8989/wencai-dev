package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.model.User;
import com.sftc.web.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 用户操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
public class UserServiceImpl extends AbstractBasicService implements UserService {

    public APIResponse login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = MD5Util.MD5(request.getParameter("password"));
        User user = new User(username, password);
        user = userMapper.selectUserByLogin(user);
        if (user == null) {
            status = APIStatus.USER_NOT_EXIST;
        } else {
            if (!password.equals(user.getPassword())) {
                status = APIStatus.USER_FAIL;
            }
        }
        return APIUtil.getResponse(status, user);
    }
}
