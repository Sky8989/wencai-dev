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
import java.util.*;

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
        User user = new User();
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wechatUser.getOpenid() != null) {
            try {
                user = userMapper.selectUserByOpenid(wechatUser.getOpenid());
            }catch (Exception e){
                e.printStackTrace();
                return APIUtil.errorResponse("有重复用户");
            }

            if (user == null) {

                user.setOpen_id(wechatUser.getOpenid());
                user.setSession_key(wechatUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                int id = 0;
                try {
                    //加入头像和昵称
                    if(userParam.getName() != null && !"".equals(userParam.getName()) && userParam.getAvatar() != null){
                        user.setAvatar(userParam.getAvatar());
                        user.setName(userParam.getName());
                        id = userMapper.insertWithAvatarAndName(user);
                        System.out.println("-   -新用户执行插入了头像和名字");
                    }else {
                        id = userMapper.insertOpenid(user);
                        System.out.println("-   -新用户没有插入了头像和名字");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return APIUtil.errorResponse("login插入失败");
                }
//                int id = userMapper.insertOpenid(user);
                String myToken = makeToken(user.getCreate_time(), user.getOpen_id());
                Token token = new Token(id, myToken);
                tokenMapper.addToken(token);
                tokenInfo.put("token", myToken);
                tokenInfo.put("user_id", (id + ""));
            } else {
                try {
                    //更新头像和昵称
                    if(userParam.getName() != null && !"".equals(userParam.getName()) && userParam.getAvatar() != null) {
                        user.setAvatar(userParam.getAvatar());
                        user.setName(userParam.getName());
                        userMapper.updateUserOfAvatar(user);
                        System.out.println("-   -老用户执行插入了头像和名字");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return APIUtil.errorResponse("user更新失败");
                }

                Token token = tokenMapper.getTokenById(user.getId());
                if (token == null) {
                    token = new Token(user.getId(), makeToken(user.getCreate_time(), user.getOpen_id()));
                    tokenMapper.addToken(token);
                } else {
                    String gmt_modified = Long.toString(System.currentTimeMillis());
                    if (Long.parseLong(gmt_modified) > Long.parseLong(token.getGmt_expiry())) {
                        token.setGmt_expiry(Long.toString(System.currentTimeMillis() + 1209600));
                        String myToken = makeToken(gmt_modified, user.getOpen_id());
                        token.setLocal_token(myToken);
                    }
                    token.setGmt_modified(gmt_modified);
                    tokenMapper.updateToken(token);
                }
                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
            }
        } else {
            status = APIStatus.WECHAT_ERR;
            status.setState(wechatUser.getErrcode().toString());
            status.setMessage(wechatUser.getErrmsg());
        }
        return APIUtil.getResponse(status, tokenInfo);
    }

    public Token getToken(int id) {
        Token token = tokenMapper.getToken(id);
        return token;
    }

    public String makeToken(String str1, String str2) {
        String s = MD5Util.MD5(str1 + str2);
        return s.substring(0, s.length() - 10);
    }
}
