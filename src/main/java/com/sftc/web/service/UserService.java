package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.Token;

public interface UserService {

    /**
     * 普通登陆
     */
    APIResponse login(APIRequest request) throws Exception;

    /**
     * 超级登陆 自动刷新token
     */
    APIResponse superLogin(APIRequest request) throws Exception;

    /**
     * 获取token的公用方法
     */
    Token getToken(int id);

    /**
     * 解除绑定操作，原微信号，解除原有手机号
     */
    APIResponse deleteMobile(APIRequest request) throws Exception;

    /**
     * 修改手机号码 即重新绑定新手机号
     */
    APIResponse updateMobile(APIRequest apiRequest) throws Exception;

    //10-12日提出的新需求 更新个人信息 作为中控给顺丰验证和更新个人信息
    APIResponse updatePersonMessage(APIRequest apiRequest) throws Exception;

    //10-23 临时Token生成接口
    APIResponse getTemporaryToken() throws Exception;

    /**
     * 下面是CMS后台所使用的接口
     */
    APIResponse selectUserListByPage(APIRequest request) throws Exception;
}
