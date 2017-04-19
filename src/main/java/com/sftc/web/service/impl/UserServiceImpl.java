package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.model.User;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 用户操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private static String AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wx6923f7053c54d11f&secret=9658591779adc2d9e333f54395e220fa&js_code=JSCODE&grant_type=authorization_code";

    public APIResponse login(APIRequest request) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        String user_phone = (String) request.getParameter("user_phone");
        String user_password = MD5Util.MD5((String) request.getParameter("user_password"));
        String open_id = (String) request.getParameter("open_id");
        User user = null;
        // 判断通过微信进行登录
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
            // 判断通过普通用户输入手机号密码登录
        } else if (user_phone != null && user_password != null) {
            user = userMapper.selectUserByLogin(new User(user_phone));
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
