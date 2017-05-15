package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIResolve;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.PAIPost;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.MessageService;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Service;

import javax.json.Json;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service("messageService")
public class MessageServiceImpl extends AbstractBasicController implements MessageService{
    private static String TAKE_MESSAGE_URL = "http://api.sf-rush.com/messages";
    private static String REGISTER_URL = "http://api.sf-rush.com/merchants";
    Gson gson = new Gson();
    public void getMessage(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        PAIPost.getPost(str, TAKE_MESSAGE_URL);
    }
    public void register(Object object){
        APIStatus status = APIStatus.SUCCESS;
        String str = gson.toJson(object);
        String result = PAIPost.getPost(str,REGISTER_URL);
        
    }
}
