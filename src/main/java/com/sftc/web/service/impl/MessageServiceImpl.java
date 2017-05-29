package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.*;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Object;

@Service("messageService")
public class MessageServiceImpl implements MessageService{

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    UserMapper userMapper;
    HttpServletRequest request;
    HttpServletResponse response;
    private static String TAKE_MESSAGE_URL = "http://api-dev.sf-rush.com/messages";
    private static String REGISTER_URL = "http://api-dev.sf-rush.com/merchants";
    private static String GET_TOKEN = "http://api-dev.sf-rush.com/merchants/me/token";
    private static String LOGIN = "http://api-dev.sf-rush.com/merchants/me";
    Gson gson = new Gson();


    /**
     * 获取短信验证码
     */
    public APIResponse getMessage(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(TAKE_MESSAGE_URL);
        String res = AIPPost.getPost(str,post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject,result.getClass());
        if(result.getErrors()!=null){
                status  = result.validateMessage();
        }
      // status= result.getError().validate();
        return APIUtil.getResponse(status,jsonObject);
    }

    /**
     * 调用顺丰注册接口
     */
    public APIResponse register(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);

        HttpPost post = new HttpPost(REGISTER_URL);
        String res = AIPPost.getPost(str,post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject,result.getClass());
        if(result.getError()!=null) {
            status = result.getError().validate();
        }
        if (result.getError()==null){
            User merchant = new User(result.getMerchant().getUuid(), result.getMerchant().getName(), result.getMerchant().getMobile(),result.getMerchant().getAvatar(),Long.toString(System.currentTimeMillis()));

            userMapper.addMerchant(merchant);
            Token token = new Token(Long.toString(System.currentTimeMillis()), Long.toString(System.currentTimeMillis()), Long.toString(System.currentTimeMillis()),Long.toString(System.currentTimeMillis()),
                  result.getToken().getAccess_token(),result.getToken().getRefresh_token(), merchant.getId(),result.getToken().getUuid());
             tokenMapper.addToken(token);
        }

        return APIUtil.getResponse(status,jsonObject);
    }

    /**
     * 获取顺丰token
     */
    public APIResponse getToken(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(GET_TOKEN);
        String res = AIPPost.getPost(str,post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject,result.getClass());

        if(result.getError()!=null){

            status = APIStatus.ERROR;
        }else {
            post.addHeader("PushEnvelope-Device-Token",result.getToken().getAccess_token());
        }
        return APIUtil.getResponse(status,jsonObject);
    }
    /**
     * 登录接口
     */
    public APIResponse login(Object object){
        APIStatus status = APIStatus.SUCCESS;
        Token token = tokenMapper.getTokenByMobile("18124033797");
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(LOGIN);
        post.addHeader("PushEnvelope-Device-Token",token.getAccess_token());
        String res = AIPPost.getPost(str,post);
        Result result = new Result();
        JSONObject jsonObject = JSONObject.fromObject(res);
       result = (Result) JSONObject.toBean(jsonObject,result.getClass());
        if(result.getError()!=null){
            status = result.getError().login();
        }
        return APIUtil.getResponse(status,jsonObject);
    }
    public APIResponse loginByGet(String object){
        APIStatus status = APIStatus.SUCCESS;
        HttpGet get = new HttpGet(LOGIN);
       get.addHeader("PushEnvelope-Device-Token", object);
        String res = APIGet.getGet(object,get);
        Result result = new Result();
        JSONObject jsonObject1 = JSONObject.fromObject(res);
        result = (Result) JSONObject.toBean(jsonObject1,result.getClass());
        if(result.getError()!=null){
            status = result.getError().login();
        }
        return APIUtil.getResponse(status,jsonObject1);
    }
}
