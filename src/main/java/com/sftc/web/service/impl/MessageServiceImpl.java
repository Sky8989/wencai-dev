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

import static com.sftc.tools.constant.SFConstant.*;

import javax.annotation.Resource;
import java.util.Map;

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
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());
        if (result.getErrors() != null) {
            status = result.validateMessage();
        }
        // status= result.getError().validate();
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 调用顺丰注册接口
     */
    public APIResponse register(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);

        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = APIPostUtil.post(str, post);
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

    /**
     * 获取顺丰token
     */
    public APIResponse getToken(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_GET_TOKEN);
        String res = APIPostUtil.post(str, post);
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

    /**
     * 登录接口
     */
    public APIResponse login(Object object) {
        APIStatus status = APIStatus.SUCCESS;
        Token token = tokenMapper.getTokenByMobile("18124033797");
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(SF_LOGIN);
        post.addHeader("PushEnvelope-Device-Token", token.getAccess_token());
        String res = APIPostUtil.post(str, post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject, result.getClass());
        if (result.getError() != null) {
            status = result.getError().login();
        }
        return APIUtil.getResponse(status, jsonObject);
    }

    /**
     * 获取个人信息
     */
    public APIResponse loginByGet(Map paramMap) {
        APIStatus status = APIStatus.SUCCESS;

        HttpGet get = new HttpGet(SF_LOGIN);
        get.addHeader("PushEnvelope-Device-Token", paramMap.get("access_token").toString());
        String res = APIGetUtil.get(get);
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
