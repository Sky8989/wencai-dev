package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResolve;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIConstant.WX_AUTHORIZATION;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    public APIResponse login(UserParam userParam) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        String auth_url = WX_AUTHORIZATION + userParam.getJs_code();
        WechatUser wechatUser = APIResolve.getWechatJson(auth_url);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wechatUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wechatUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                User user2 = new User();
                user2.setOpen_id(wechatUser.getOpenid());
                user2.setSession_key(wechatUser.getSession_key());
                user2.setCreate_time(Long.toString(System.currentTimeMillis()));
                //加入头像和昵称
                if (userParam.getName() != null && userParam.getAvatar() != null) {
                    user2.setAvatar(userParam.getAvatar());
                    user2.setName(userParam.getName());
                    userMapper.insertWithAvatarAndName(user2);
                    //System.out.println("-   -新用户执行插入了头像和名字，用户的id是："+user2.getId());
                } else {
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
                if (userParam.getName() != null && userParam.getAvatar() != null) {
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
            return APIUtil.submitErrorResponse(wechatUser.getErrmsg(), wechatUser);
        }
        return APIUtil.getResponse(status, tokenInfo);
    }

    public Token getToken(int id) {
        return tokenMapper.getToken(id);
    }

    private String makeToken(String str1, String str2) {
        String s = MD5Util.MD5(str1 + str2);
        return s.substring(0, s.length() - 10);
    }
}
