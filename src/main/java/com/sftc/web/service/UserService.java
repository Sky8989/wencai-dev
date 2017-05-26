package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Token;
import com.sftc.web.model.wechat.WechatUser;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 用户操作接口
 * @date 17/4/1
 * @Time 下午9:32
 */
public interface UserService {

    /**
     * 登录
     * @param wechatUser
     * @return
     */
    APIResponse login(WechatUser wechatUser) throws Exception;
    Token getToken(int id);
}
