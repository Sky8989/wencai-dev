package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;


public interface MessageService {

    // 获取验证码短信
    APIResponse getMessage(APIRequest apiRequest);

    // 用户注册
    APIResponse register(APIRequest apiRequest);

    //如商家token过期，访问此接口获取新的token 密码或验证码二者取一
    APIResponse getToken(APIRequest apiRequest);

    APIResponse sfLogin(APIRequest apiRequest);

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param touser_id  接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
//    void sendWXTemplateMessage(int touser_id, String[] messageArr, String pagePath, String form_id, String template_id);

    /**
     * 获取图片验证码
     *
     * @return APIResponse
     */
    APIResponse captchas();

}
