package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.api.AIPPost;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.User;
import com.sftc.web.model.Result;
import com.sftc.web.service.MessageService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("messageService")
public class MessageServiceImpl extends AbstractBasicController implements MessageService{
    @Resource
    UserMapper userMapper;

    private static String TAKE_MESSAGE_URL = "http://api.sf-rush.com/messages";
    private static String REGISTER_URL = "http://api.sf-rush.com/merchants";
    private static String GET_TOKEN = "http://api.sf-rush.com/merchants/me/token";
    private static String LOGIN = "http://api.sf-rush.com/merchants/me";
    Gson gson = new Gson();


    /**
     * 获取短信验证码
     */
    public APIResponse getMessage(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = AIPPost.getPost(str, TAKE_MESSAGE_URL, new Result());
        if(result.getErrors()!=null){
            System.out.println("哈哈");
                status  = result.validateMessage();
        }
      // status= result.getError().validate();
        return APIUtil.getResponse(status,null);
    }

    /**
     * 调用顺丰注册接口
     */
    public APIResponse register(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = AIPPost.getPost(str, REGISTER_URL, new Result());
        status= result.getError().validate();
        if (status.equals(APIStatus.REGISTER_SUCCESS)){
            User user = new User("123456", "1", "2017-7-7");
            userMapper.addMerchant(user);
        }
        return APIUtil.getResponse(status,null);
    }

    /**
     * 获取顺丰token
     */
    public APIResponse getToken(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = AIPPost.getPost(str, GET_TOKEN, new Result());
        if(result.getError()!=null){
            status = APIStatus.ERROR;
        }
        return APIUtil.getResponse(status,null);
    }
    /**
     * 登录接口
     */
    public APIResponse login(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = AIPPost.getPost(str, LOGIN, new Result());
        if(result.getError()!=null){
            status = result.getError().login();
        }
        return APIUtil.getResponse(status,null);
    }
}
