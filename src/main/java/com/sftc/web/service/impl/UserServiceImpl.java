package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
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
        String js_code = (String) request.getParameter("js_code");
        User user = null;
        AUTHORIZATION_URL = AUTHORIZATION_URL.replace("JSCODE", js_code);
        WechatUser wechatUser = APIResolve.getJson(AUTHORIZATION_URL);
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
            return APIUtil.getResponse(status, user);
    }

    public Token getToken(int id) {
        Token token = tokenMapper.getToken(id);
        return token;
    }
}
