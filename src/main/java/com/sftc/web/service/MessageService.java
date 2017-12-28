package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.*;


public interface MessageService {

    // 获取验证码短信
    ApiResponse getMessage(SMSMessageRequestVO apiRequest);

    // 用户注册
    ApiResponse register(RegisterRequestVO apiRequest);

    //如商家token过期，访问此接口获取新的token 密码或验证码二者取一
    ApiResponse getToken(GetTokenRequestVO apiRequest);

    ApiResponse sfLogin(SFLoginRequestVO body);

    ApiResponse messageCheck(UserValidateVO apiRequest);

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param userUUID  接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
   void sendWXTemplateMessage(String userUUID, String[] messageArr, String pagePath, String form_id, String template_id);

    /**
     * 获取图片验证码
     *
     * @return ApiResponse
     */
    ApiResponse captchas();

}
