package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.wechat.WechatUser;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Service;

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
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    protected TokenMapper tokenMapper;
    
    public APIResponse login(UserParam userParam) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        String AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wxb6cbb81471348fec&secret=b201962b8a3da757c72a0747eb6f1110&js_code=JSCODE&grant_type=authorization_code";
        AUTHORIZATION_URL = AUTHORIZATION_URL.replace("JSCODE", userParam.getJs_code());
        WechatUser wechatUser = APIResolve.getWechatJson(AUTHORIZATION_URL);
        User user = null;
        if (wechatUser.getOpenid() != null) {
            user = userMapper.selectUserByOpenid(wechatUser.getOpenid());
            if (user == null) {
                user = new User();
                user.setOpen_id(wechatUser.getOpenid());
                user.setSession_key(wechatUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                int id = userMapper.insertOpenid(user);
                String myToken = makeToken(user.getOpen_id(), user.getSession_key());
                Token token = new Token(id, myToken);
                user.setId(id);
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

    public String makeToken(String str1, String str2) {
        return MD5Util.MD5(str1 + str2);
    }
}
