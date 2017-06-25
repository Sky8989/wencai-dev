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
        //真实的wechatUser
        String AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wxb6cbb81471348fec&secret=b201962b8a3da757c72a0747eb6f1110&js_code=JSCODE&grant_type=authorization_code";
        AUTHORIZATION_URL = AUTHORIZATION_URL.replace("JSCODE", userParam.getJs_code());
        WechatUser wechatUser = APIResolve.getWechatJson(AUTHORIZATION_URL);
        //构建测试用的 wechatUser
//        WechatUser wechatUser = new WechatUser();
//        //wechatUser.setOpenid("openidTest123"+new Random().nextInt());
//        wechatUser.setOpenid("openidTest123497150228");
//        wechatUser.setSession_key("session_keyTest123"+new Random().nextInt());
//        wechatUser.setExpires_in("expires_inTest123"+new Random().nextInt());
//        wechatUser.setErrcode(4);
//        wechatUser.setErrmsg("请求微信的过程出现了错误");

        User user = null;
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wechatUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wechatUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.errorResponse("出现重复用户，请填写用户反馈");
            }if (userList.size() == 1){
                user = userList.get(0);
            }
            if (user == null) {
                User user2 = new User();
                user2.setOpen_id(wechatUser.getOpenid());
                user2.setSession_key(wechatUser.getSession_key());
                user2.setCreate_time(Long.toString(System.currentTimeMillis()));
                    //加入头像和昵称
                    if(userParam.getName() != null  && userParam.getAvatar() != null){
                        user2.setAvatar(userParam.getAvatar());
                        user2.setName(userParam.getName());
                        userMapper.insertWithAvatarAndName(user2);
                        //System.out.println("-   -新用户执行插入了头像和名字，用户的id是："+user2.getId());
                    }else {
                        userMapper.insertOpenid(user2);
                        //System.out.println("-   -新用户没有插入了头像和名字,用户的id是："+user2.getId());
                    }
                    //构建新的token
                    String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
                    Token token = new Token(user2.getId(), myToken);
                    tokenMapper.addToken(token);
                    tokenInfo.put("token", myToken);
                    tokenInfo.put("user_id", (user2.getId() + ""));
//                int id = userMapper.insertOpenid(user);
//                String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
//                Token token = new Token(id, myToken);
//                tokenMapper.addToken(token);
//                tokenInfo.put("token", myToken);
//                tokenInfo.put("user_id", (id + ""));
            } else {
                user.setOpen_id(wechatUser.getOpenid());
                user.setSession_key(wechatUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                //更新头像和昵称
                if(userParam.getName() != null  && userParam.getAvatar() != null) {
                    user.setAvatar(userParam.getAvatar());
                    user.setName(userParam.getName());
                    userMapper.updateUserOfAvatar(user);
                    System.out.println("-   -老用户执行插入了头像和名字");
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
