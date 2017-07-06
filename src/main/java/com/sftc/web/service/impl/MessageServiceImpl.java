package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.sftc.tools.constant.SFConstant.*;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private UserMapper userMapper;

    private Gson gson = new Gson();

    /**
     * 获取短信验证码
     */
    public APIResponse getMessage(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        String res = APIPostUtil.post(str, post);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.containsKey("error")) {
            status = APIStatus.VALIDATION_ERROR;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 用户注册
     */
    public APIResponse register(Object object) {
        APIStatus status = APIStatus.SUCCESS;

        JSONObject jsonObject = JSONObject.fromObject(object);
        int user_id = jsonObject.getInt("user_id");
        jsonObject.remove("user_id");
        // 调用顺丰接口
        String str = gson.toJson(jsonObject);
        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = APIPostUtil.post(str, post);
        JSONObject resJSONObject = JSONObject.fromObject(res);

        // 注册失败 匹配error
        if (resJSONObject.getString("error") != null) {
            return APIUtil.submitErrorResponse("注册失败，见返回值", resJSONObject);
        } else {

            // 注册成功
            JSONObject merchantJSONObject = resJSONObject.getJSONObject("merchant");
            JSONObject tokenJSONObject = resJSONObject.getJSONObject("token");

            // 存储 token中的access_token 到token表
            Token token = new Token();
            token.setUser_id(user_id);
            token.setAccess_token(tokenJSONObject.getString("access_token"));
            token.setRefresh_token(tokenJSONObject.getString("refresh_token"));
            tokenMapper.updateToken(token);

            // 存储 merchant中的uuid 到user表
            User user = new User();
            user.setId(user_id);
            user.setMobile(merchantJSONObject.getString("mobile"));
            user.setUuid(merchantJSONObject.getString("uuid"));
            userMapper.updateUser(user);

            return APIUtil.getResponse(status, resJSONObject);
        }
    }

    /**
     * 获取顺丰token 需要传入手机号和验证码
     */
    public APIResponse getToken(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = JSONObject.fromObject(object);

        if (jsonObject.containsKey("user_id")) {
            int user_id = jsonObject.getInt("user_id");
            jsonObject.remove("user_id");
            // 生成sf获取token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            if (jsonObject.containsKey("refresh_token") && !"".equals(jsonObject.get("refresh_token"))){
                postUrl.append("?refresh_token=");
                postUrl.append(jsonObject.get("refresh_token"));
            }
            // 去除无用的json节点
            jsonObject.remove("refresh_token");

            // 调用顺丰接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(postUrl.toString());
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

            if (!resJSONObject.containsKey("error")) {
                // 更新 token中的access_token 到token表
                String access_token = resJSONObject.getJSONObject("token").getString("access_token");
                String refresh_token = resJSONObject.getJSONObject("token").getString("refresh_token");
                Token token = new Token();
                token.setUser_id(user_id);
                token.setAccess_token(access_token);
                token.setRefresh_token(refresh_token);
                tokenMapper.updateToken(token);
                return APIUtil.getResponse(status, resJSONObject);
            } else {
                return APIUtil.submitErrorResponse("token获取失败，请参考错误信息", resJSONObject);
            }
        } else {
            return APIUtil.paramErrorResponse("缺少参数，user_id");
        }
    }

    /**
     * 登陆 专指登陆顺丰的接口
     */
    public APIResponse sfLogin(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = JSONObject.fromObject(object);
        int user_id = jsonObject.getInt("user_id");
        String sfToken = jsonObject.getString("token");
        jsonObject.remove("user_id");
        jsonObject.remove("token");

        if (sfToken != null) {
            //调用顺丰的登陆接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(SF_LOGIN);
            post.addHeader("PushEnvelope-Device-Token", sfToken);
            String res = APIPostUtil.post(str, post);

            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey("error")) {
                return APIUtil.submitErrorResponse("LOGIN_ERROR", resJSONObject);
            } else {
                // 更新 uuid
                User user = userMapper.selectUserByUserId(user_id);
                if (user != null && (user.getUuid() == null || "".equals(user.getUuid()))) {
                    user.setUuid(resJSONObject.getJSONObject("merchant").getString("uuid"));
                    userMapper.updateUser(user);
                }
            }
            return APIUtil.getResponse(status, resJSONObject);
        } else {
            return APIUtil.paramErrorResponse("缺少参数，请传入sfToken");
        }
    }

}
