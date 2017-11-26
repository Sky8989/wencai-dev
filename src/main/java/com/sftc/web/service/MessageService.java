package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;


public interface MessageService {

    /**
     * 获取手机验证码
     * @param apiRequest
     * @return
     */
    APIResponse getMessage(APIRequest apiRequest);

    /**
     * 验证手机验证码
     * @param apiRequest
     * @return
     */
    APIResponse messageCheck(APIRequest apiRequest);

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param touser_id  接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
   void sendWXTemplateMessage(int touser_id, String[] messageArr, String pagePath, String form_id, String template_id);

}
