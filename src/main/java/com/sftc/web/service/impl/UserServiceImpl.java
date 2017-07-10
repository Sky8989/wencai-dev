package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.wechat.WechatUser;
import com.sftc.web.service.UserService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.constant.SFConstant.SF_GET_TOKEN;
import static com.sftc.tools.constant.SFConstant.SF_LOGIN;
import static com.sftc.tools.constant.ThirdPartyConstant.WX_AUTHORIZATION;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    private static final String postStr= "{\"merchant\":{" +
            "\"mobile\":\"13797393543\"" + "}," +
            "\"message\":{" + "\"content\":\"4444\"}}" ;

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
                } else {
                    userMapper.insertOpenid(user2);
                }
                //构建新的token
                String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
                Token token = new Token(user2.getId(), myToken);
                tokenMapper.addToken(token);
                tokenInfo.put("token", myToken);
                tokenInfo.put("user_id", (user2.getId() + ""));
            } else {
                user.setOpen_id(wechatUser.getOpenid());
                user.setSession_key(wechatUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                //更新头像和昵称
                if (userParam.getName() != null && userParam.getAvatar() != null) {
                    user.setAvatar(userParam.getAvatar());
                    user.setName(userParam.getName());
                    userMapper.updateUserOfAvatar(user);
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
    //  合并后的登陆接口 前台来一个jscode
    public APIResponse superLogin(UserParam userParam) throws Exception {
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
                } else {
                    userMapper.insertOpenid(user2);
                }
                //构建新的token
                String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
                Token token = new Token(user2.getId(), myToken);
                tokenMapper.addToken(token);
                tokenInfo.put("token", myToken);
                tokenInfo.put("user_id", (user2.getId() + ""));
            } else {
                user.setOpen_id(wechatUser.getOpenid());
                user.setSession_key(wechatUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                //更新头像和昵称
                if (userParam.getName() != null && userParam.getAvatar() != null) {
                    user.setAvatar(userParam.getAvatar());
                    user.setName(userParam.getName());
                    userMapper.updateUserOfAvatar(user);
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
                    // 此处更新 localtoken
                    tokenMapper.updateToken(token);
                }
                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
                // 获取顺丰 access_token和uuid的内容
                HashMap<String,String> map = checkAccessToken(token.getUser_id());
                if (map.containsKey("error")){
                    return APIUtil.submitErrorResponse("refresh_token failed",JSONObject.fromObject(map.get("error")));
                }
                if (map.containsKey("uuid"))
                {tokenInfo.put("uuid",map.get("uuid"));} else tokenInfo.put("uuid",user.getUuid());

                tokenInfo.put("access_token",map.get("access_token"));
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

    // 验证access_token是否有效 通过访问merchant/me接口 但只针对有access_token的用户
    private HashMap<String,String> checkAccessToken(int user_id){
        Token token = tokenMapper.getTokenById(user_id);
        //验证 access_token 如果error则用refresh_token
        String old_accesstoken = token.getAccess_token();
        JSONObject resJSONObject = catchSFLogin(old_accesstoken);
        HashMap<String,String> map = new HashMap<String,String>();
        if (resJSONObject.containsKey("error")) { // 旧的token失效 要刷新
            // 访问sf刷新token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            postUrl.append("?refresh_token=");
            postUrl.append(token.getRefresh_token());
            HttpPost post = new HttpPost(postUrl.toString());
            String resPost = APIPostUtil.post(postStr, post);
            JSONObject resPostJSONObject = JSONObject.fromObject(resPost);
            // 处理refresh刷新失败的情况
            if (resPostJSONObject.containsKey("error")){
                map.put("error",resPost);
                return map;
            }
            // 更新 两个关键token
            String newAccess_token = resPostJSONObject.getJSONObject("token").getString("access_token");
            String newRefresh_token = resPostJSONObject.getJSONObject("token").getString("refresh_token");
            token.setAccess_token(newAccess_token);
            token.setRefresh_token(newRefresh_token);
            tokenMapper.updateToken(token);


            map.put("access_token",newAccess_token);
        }else{ // 旧的token有效
            map.put("uuid",resJSONObject.getJSONObject("merchant").getString("uuid"));
            map.put("access_token",old_accesstoken);
        }
            return map;
    }
    // 通过access_token访问sf接口 获取uuid的公告方法
    private JSONObject catchSFLogin(String old_accesstoken) {
        HttpGet httpGet = new HttpGet(SF_LOGIN);
        httpGet.addHeader("PushEnvelope-Device-Token",old_accesstoken);
        String res = APIGetUtil.get(httpGet);
        return JSONObject.fromObject(res);
    }

    /**
     * 下面是CMS的内容
     */
    public APIResponse selectUserListByPage(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        HttpServletRequest httpServletRequest = request.getRequest();
        // 此处封装了 User的构造方法
        User user = new User(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<User> userList = userMapper.selectByPage(user);
        if (userList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(status, userList);
        }
    }
}
