package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.api.PAIPost;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Merchant;
import com.sftc.web.model.Result;
import com.sftc.web.service.MessageService;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2017/5/15.
 */
@Service("messageService")
public class MessageServiceImpl extends AbstractBasicController implements MessageService{
    @Resource
    UserMapper userMapper;

    private static String TAKE_MESSAGE_URL = "http://api.sf-rush.com/messages";
    private static String REGISTER_URL = "http://api.sf-rush.com/merchants";
    private static String GET_TOKEN = "http://api.sf-rush.com/merchants/me/token";
    private static String LOGIN = "http://api.sf-rush.com/merchants/me";
    Gson gson = new Gson();
    public APIResponse getMessage(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = PAIPost.getPost(str, TAKE_MESSAGE_URL,new Result());
       status= result.getError().validate();
        return APIUtil.getResponse(status,null);
    }

    /**
     * 调用顺丰注册接口
     */
    public APIResponse register(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = PAIPost.getPost(str,REGISTER_URL,new Result());
        status= result.getError().validate();
        if (status.equals(APIStatus.REGISTER_SUCCESS)){
            Merchant merchant = new Merchant("123456", "1", "2017-7-7");
            userMapper.addMerchant(merchant);
        }
        return APIUtil.getResponse(status,null);
    }

    /**
     * 获取顺丰token
     */
    public APIResponse getToken(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        Result result = PAIPost.getPost(str,GET_TOKEN,new Result());
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
        Result result = PAIPost.getPost(str,LOGIN,new Result());
        if(result.getError()!=null){
            status = result.getError().login();
        }
        return APIUtil.getResponse(status,null);
    }
}
