package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.MessageMapper;
import com.sftc.web.model.Message;
import com.sftc.web.service.NotificationMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxingyue on 2017/6/27.
 */
@Service
public class NotificationMessageServiceImpl implements NotificationMessageService {
    @Resource
    private MessageMapper messageMapper;

    public APIResponse getMessage(APIRequest apiRequest) {
        APIStatus status = APIStatus.SUCCESS;
        String s = (String) apiRequest.getRequestParam().toString();
        System.out.println("-   -"+s);
        Message message = new Gson().fromJson(s,Message.class);
        List<Message> listone = messageMapper.selectMessageReceiveAddress(message.getUser_id());
        List<Message> listtwo = messageMapper.selectMessageReceiveExpress(message.getUser_id());
        JsonObject jsonObject = new JsonObject();
        ArrayList<Message> messageList = new ArrayList<Message>();
        if (!listone.isEmpty() && listone.get(0).getIs_read() == 0){
                messageList.add(listone.get(0));
        }
        if (!listtwo.isEmpty() && listtwo.get(0).getIs_read() == 0){
            messageList.add(listtwo.get(0));
        }
        jsonObject.add("messages",new Gson().toJsonTree(messageList));
        return APIUtil.getResponse(status,jsonObject);
    }

    public APIResponse updateMessage(APIRequest apiRequest) {
        return null;
    }
}
