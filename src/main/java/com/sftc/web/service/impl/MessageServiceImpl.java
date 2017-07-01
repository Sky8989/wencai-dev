package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Result;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.sftc.tools.api.APIConstant.*;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private UserMapper userMapper;

    private Gson gson = new Gson();

    /**
     * Author:hxy starmoon1994
     * Description:获取短信验证码
     * Date:14:41 2017/6/30
     */
    public APIResponse getMessage(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        String res = AIPPost.getPost(str, post);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.containsKey("error")) {
            status = APIStatus.VALIDATION_ERROR;
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 注册 * // POST /merchants 用户注册 message.content 内容是短信发出的数字验证码
     * todo 注册接口缓存uuid和token
     */
    public APIResponse register(Object object) {
        APIStatus status = APIStatus.SUCCESS;

        JSONObject jsonObject = JSONObject.fromObject(object);
        int user_id = jsonObject.getInt("user_id");
        jsonObject.remove("user_id");
        // 调用顺丰接口
        String str = gson.toJson(jsonObject);
        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = AIPPost.getPost(str, post);
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
     * hxy starmoon1994
     * todo 获取 顺丰token 需要传入手机号和验证码
     * 14:41 2017/6/30
     */
    public APIResponse getToken(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = JSONObject.fromObject(object);

        if (jsonObject.containsKey("user_id")){
            int user_id = jsonObject.getInt("user_id");
            jsonObject.remove("user_id");
            // 调用顺丰接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(SF_GET_TOKEN);
            String res = AIPPost.getPost(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

            if (!resJSONObject.containsKey("error")) {
                // 更新 token中的access_token 到token表
                String access_token = resJSONObject.getJSONObject("token").getString("access_token");
                Token token = new Token();
                token.setUser_id(user_id);
                token.setAccess_token(access_token);
                tokenMapper.updateToken(token);
                return APIUtil.getResponse(status, resJSONObject);
            } else {
                return APIUtil.submitErrorResponse("token获取失败，请参考错误信息", resJSONObject);
            }
        }else {
            return APIUtil.paramErrorResponse("缺少参数，user_id");
        }
    }

    /**
     * todo 登陆  专指登陆顺丰的接口
     */
    public APIResponse sfLogin(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        JSONObject jsonObject = JSONObject.fromObject(object);
        int user_id = jsonObject.getInt("user_id");
        String sfToken = jsonObject.getString("token");
        jsonObject.remove("user_id");
        jsonObject.remove("token");

        if (sfToken != null){
            //调用顺丰的登陆接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(SF_LOGIN);
            post.addHeader("PushEnvelope-Device-Token",sfToken);
            String res = AIPPost.getPost(str, post);

            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey("error")) {
               return APIUtil.submitErrorResponse("LOGIN_ERROR",resJSONObject);
            }else {
                // 更新 uuid
                User user = userMapper.selectUserByUserId(user_id);
                if(user != null && (user.getUuid()==null || "".equals(user.getUuid()))){
                    user.setUuid(resJSONObject.getJSONObject("merchant").getString("uuid"));
                    userMapper.updateUser(user);
                }
            }
            return APIUtil.getResponse(status,resJSONObject);
        }else {
            return APIUtil.paramErrorResponse("缺少参数，请传入sfToken");
        }
    }


    /* *//**
     * 获取短信验证码
     *//*
    public APIResponse getMessage(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        String res = AIPPost.getPost(str, post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());
        if (result.getErrors() != null) {
            status = result.validateMessage();
        }
        // status= result.getError().validate();
        return APIUtil.getResponse(status, jsonObject);
    }

    *//**
     * 调用顺丰注册接口
     *//*
    public APIResponse register(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);

        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = AIPPost.getPost(str, post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());
        if (result.getError() != null) {
            status = result.getError().validate();
        }
        if (result.getError() == null) {
            User merchant = new User(result.getMerchant().getUuid(), result.getMerchant().getName(), result.getMerchant().getMobile(), result.getMerchant().getAvatar(), Long.toString(System.currentTimeMillis()));

            userMapper.addMerchant(merchant);
            Token token = new Token(Long.toString(System.currentTimeMillis()), Long.toString(System.currentTimeMillis()), Long.toString(System.currentTimeMillis()), Long.toString(System.currentTimeMillis()),
                    result.getToken().getAccess_token(), result.getToken().getRefresh_token(), merchant.getId(), result.getToken().getUuid());
            tokenMapper.addToken(token);
        }

        return APIUtil.getResponse(status, jsonObject);
    }

    *//**
     * 获取顺丰token
     *//*
    public APIResponse getToken(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_GET_TOKEN);
        String res = AIPPost.getPost(str, post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());

        if (result.getError() != null) {

            status = APIStatus.ERROR;
        } else {
            post.addHeader("PushEnvelope-Device-Token", result.getToken().getAccess_token());
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    *//**
     * 登录接口
     *//*
    public APIResponse login(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        Token token = tokenMapper.getTokenByMobile("18124033797");
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_LOGIN);
        post.addHeader("PushEnvelope-Device-Token", token.getAccess_token());
        String res = AIPPost.getPost(str, post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());
        if (result.getError() != null) {
            status = result.getError().login();
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    */

    /**
     *
     */
    public APIResponse loginByGet(Map paramMap) {
        APIStatus status = APIStatus.SUCCESS;

        HttpGet get = new HttpGet(SF_LOGIN);
        get.addHeader("PushEnvelope-Device-Token", paramMap.get("access_token").toString());
        String res = APIGet.getGet(get);
        JSONObject jsonObject1 = JSONObject.fromObject(res);

        if (jsonObject1.get("errors") != null || jsonObject1.get("error") != null) {
            status = APIStatus.LOGIN_FAIL;
        }

        if (paramMap.containsKey("name") && paramMap.containsKey("avatar")) {
            User user = new User();
            user.setName(paramMap.get("name").toString());
            user.setAvatar(paramMap.get("avatar").toString());
        }

        return APIUtil.getResponse(status, jsonObject1);
    }
}
