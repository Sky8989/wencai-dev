package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.service.TokenService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.HashMap;

import static com.sftc.tools.constant.SFConstant.SF_GET_TOKEN;
import static com.sftc.tools.constant.SFConstant.SF_LOGIN;
import static com.sftc.tools.constant.SFConstant.SF_REGISTER_URL;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    private static final String postStr = "{\"merchant\":{" +
            "\"mobile\":\"13797393543\"" + "}," +
            "\"message\":{" + "\"content\":\"4444\"}}";

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    /**
     * token机制的中控器
     *
     * @param apiRequest
     * @return
     * @throws Exception
     */
    public APIResponse token(APIRequest apiRequest) throws Exception {
        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        ///当有参数system_refresh_token时，提供token刷新服务

        if (paramOBJ.containsKey("system_refresh_token")) {

            if (!"".equals(paramOBJ.getString("system_refresh_token"))) {
                return this.refreshToken(paramOBJ.getString("system_refresh_token"));
            } else {
                return APIUtil.paramErrorResponse("system_refresh_token can't been ''");
            }

        } else if (paramOBJ.containsKey("mobileLogin")) {
            this.mobileGetToken(paramOBJ.getJSONObject("mobileLogin"));
        } else if (paramOBJ.containsKey("wxLogin")) {
            this.WXGetToken(paramOBJ.getJSONObject("wxLogin"));
        }

        return APIUtil.paramErrorResponse("Param_error");

    }

    //////////////////////////private methods//////////////////////////

    /**
     * 通过微信登陆来获取token
     *
     * @return APIResponse
     */
    private APIResponse WXGetToken(JSONObject paramOBJ) {
        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }


    /**
     * 通过手机号加验证码来获取token
     *
     * @return APIResponse
     */
    private APIResponse mobileGetToken(JSONObject mobileLogin) {
        //获取手机号 短信验证码 发送注册请求   提取返回值

        // handle param
        String mobile;
        String content;
        if (mobileLogin.containsKey("mobile") && !mobileLogin.containsKey("content")) {
            mobile = mobileLogin.getString("mobile");
            content = mobileLogin.getString("content");
        } else {
            return APIUtil.paramErrorResponse("mobile_param_error");
        }

        // switch 2 branchs
        User user = userMapper.selectUserByPhone(mobile);
        if (user == null) { // 新用户 则需要走sf注册流程
            //走一遍登陆接口
            JSONObject sfLoginOBJ = JSONObject.fromObject(postStr);
            sfLoginOBJ.getJSONObject("merchant").remove("mobile");
            sfLoginOBJ.getJSONObject("merchant").put("mobile", mobile);
            sfLoginOBJ.getJSONObject("message").remove("content");
            sfLoginOBJ.getJSONObject("merchant").put("content", content);


            //走一遍注册接口


        } else {// 老用户  刷新token 返回token即可

        }


        return null;
    }

    /**
     * 刷新token的入口
     *
     * @return APIResponse
     */
    private APIResponse refreshToken(String system_refresh_token) {
        System.out.println("-   -system_refresh_token:" + system_refresh_token);
        // 刷新本地token


        // 刷新sf相关的token


        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }

    // 验证access_token是否有效 通过访问merchant/me接口 但只针对有access_token的用户
    private HashMap<String, String> checkAccessToken(int user_id, Token paramtoken) throws Exception {
        Token token = tokenMapper.getTokenById(user_id);
        //验证 access_token 如果error则用refresh_token
        String old_accesstoken = token.getAccess_token();
        JSONObject resJSONObject = catchSFLogin(old_accesstoken);
        HashMap<String, String> map = new HashMap<String, String>();
        if (resJSONObject.containsKey("error")) { // 旧的token失效 要刷新
            // 访问sf刷新token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            postUrl.append("?refresh_token=");
            postUrl.append(token.getRefresh_token());
            HttpPost post = new HttpPost(postUrl.toString());
            String resPost = APIPostUtil.post(postStr, post);
            JSONObject resPostJSONObject = JSONObject.fromObject(resPost);
            // 处理refresh刷新失败的情况
            if (resPostJSONObject.containsKey("error")) {
                map.put("error", resPost);
                return map;
            }
            // 更新 两个关键token
            String newAccess_token = resPostJSONObject.getJSONObject("token").getString("access_token");
            String newRefresh_token = resPostJSONObject.getJSONObject("token").getString("refresh_token");
            token.setAccess_token(newAccess_token);
            token.setRefresh_token(newRefresh_token);
            tokenMapper.updateToken(token);


            map.put("access_token", newAccess_token);
        } else { // 旧的token有效
            map.put("uuid", resJSONObject.getJSONObject("merchant").getString("uuid"));
            map.put("access_token", old_accesstoken);
        }
        return map;
    }

    // 通过access_token访问sf接口 获取uuid的公告方法
    private JSONObject catchSFLogin(String old_accesstoken) throws Exception {
        HttpGet httpGet = new HttpGet(SF_LOGIN);
        httpGet.addHeader("PushEnvelope-Device-Token", old_accesstoken);
        String res = APIGetUtil.get(httpGet);
        return JSONObject.fromObject(res);
    }
    //////////////////////////Task methods//////////////////////////

    //一个定时刷新token的调度任务


    //////////////////////////utils methods//////////////////////////
    private String makeToken() {
        String str1 = Long.toString(System.currentTimeMillis());
        String str2 = RandomStringUtils.random(3, "QWERTYUIOPASDFGHJKLZXCVBNM");
        ;
        String s = MD5Util.MD5(str1 + str2);
        return s.substring(0, s.length() - 10);
    }
}
