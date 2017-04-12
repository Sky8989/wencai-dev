package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 用户操作接口
 * @date 17/4/1
 * @Time 下午9:32
 */
public interface UserService {

    /**
     * 登录
     * @param request 获取请求参数
     * @return
     */
    APIResponse login(HttpServletRequest request) throws Exception;
}
