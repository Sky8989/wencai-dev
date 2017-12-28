package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantsRequestVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;

public interface UserService {

    /**
     * 普通登陆
     * @param body
     */
    ApiResponse login(UserParamVO body) throws Exception;

    /**
     * 超级登陆 自动刷新token
     * @param body
     */
    ApiResponse superLogin(UserParamVO body) throws Exception;

    /**
     * 获取token的公用方法
     */
    Token getToken(int id);
    /**
     * 更新商户信息
     * @param body
     */
    ApiResponse updatePersonMessage(UserMerchantsRequestVO body) throws Exception;

    /**
     * 检查账号是否已经绑定手机号
     */
    ApiResponse checkBindStatus() throws Exception;

    /**
     * 获取用户钱包
     */
    ApiResponse obtainUserWallets(int type);

    /**
     * 获取余额明细
     */
    ApiResponse obtainBalanceDetailed(int limit, int offset);
}
