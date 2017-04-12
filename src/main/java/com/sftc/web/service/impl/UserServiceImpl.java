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

    private static String AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    @Override
    public APIResponse login(HttpServletRequest request) throws Exception {
        String user_phone = request.getParameter("user_phone");
        String user_password = MD5Util.MD5(request.getParameter("user_password"));
        String open_id = request.getParameter("open_id");
        User user = null;
        if (open_id != null) {
            user.setOpen_id(open_id);
            user = userMapper.selectUserByLogin(user);
            if (user == null) {
                AUTHORIZATION_URL = AUTHORIZATION_URL.replace("JSCODE", open_id);
                open_id = APIResolve.getJson(AUTHORIZATION_URL, "openid");
                userMapper.insertOpenid(open_id);
            } else {
                status = APIStatus.FAIL;
            }
        } else if (user_phone != null && user_password != null) {
            user = new User(user_phone, user_password);
            user = userMapper.selectUserByLogin(user);
            if (user == null) {
                status = APIStatus.USER_NOT_EXIST;
            } else {
                if (!user_password.equals(user.getUser_password())) {
                    status = APIStatus.USER_FAIL;
                }
            }
        }
        return APIUtil.getResponse(status, user);
    }
}
