package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;

import com.sftc.web.model.User;

import com.sftc.web.model.Token;

import com.sftc.web.model.wechat.WechatUser;
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

    private static String AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wxb6cbb81471348fec&secret=b201962b8a3da757c72a0747eb6f1110&js_code=JSCODE&grant_type=authorization_code";

    public APIResponse login(APIRequest request) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        String user_phone = (String) request.getParameter("user_phone");
        String user_password = MD5Util.MD5((String) request.getParameter("user_password"));
        String js_code = (String) request.getParameter("js_code");
        User user = null;
        WechatUser wechatUser = null;
        // 判断通过微信进行登录
        if (js_code != null) {
            AUTHORIZATION_URL = AUTHORIZATION_URL.replace("JSCODE", js_code);
            wechatUser = APIResolve.getJson(AUTHORIZATION_URL);
            System.out.println(wechatUser.getOpenid());
            if (wechatUser.getOpenid() != null) {
                user = userMapper.selectUserByOpenid(wechatUser.getOpenid());
                if (user == null) {
                    userMapper.insertOpenid(wechatUser.getOpenid());
                }
            } else {
                status = APIStatus.WECHAT_ERR;
                status.setState(wechatUser.getErrcode().toString());
                status.setMessage(wechatUser.getErrmsg());
            }

            // 判断通过普通用户输入手机号密码登录
        } else if (user_phone != null && user_password != null) {
            user = userMapper.selectUserByPhone(user_phone);
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

    public Token getToken(int id) {
        Token token = tokenMapper.getToken(id);
        return token;
    }
}
