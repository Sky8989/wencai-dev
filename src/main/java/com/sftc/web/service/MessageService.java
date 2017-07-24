package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

import com.sftc.web.model.Result;
import org.codehaus.jackson.map.util.JSONPObject;

import java.util.Map;


public interface MessageService {

    // 获取验证码短信
    APIResponse getMessage(Object object);

    // 用户注册
    APIResponse register(Object object);

    //如商家token过期，访问此接口获取新的token 密码或验证码二者取一
    APIResponse getToken(Object object);

    APIResponse sfLogin(Object object);

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param touser_id  接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
    void sendWXTemplateMessage(int touser_id, String[] messageArr, String pagePath, String form_id,String template_id);

}
