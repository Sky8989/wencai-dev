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
}
