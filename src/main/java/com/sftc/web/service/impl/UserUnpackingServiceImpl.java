package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.redis.UserUnpackingRedis;
import com.sftc.web.model.User;
import com.sftc.web.service.UserUnpackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import static com.sftc.tools.api.APIStatus.SELECT_FAIL;
import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class UserUnpackingServiceImpl implements UserUnpackingService {

    @Autowired
    private UserUnpackingRedis userUnpackingRedis;
    @Resource
    private TokenMapper tokenMapper;

    private static final String  DELIMITER = "_";
    private static final String HEARD_TOKEN = "token";

    @Override
    public APIResponse unpacking(String order_id, HttpServletRequest request, int type) {
        String token = request.getHeader(HEARD_TOKEN);
        User user = tokenMapper.tokenInterceptor(token);
        if (user==null){
            return APIUtil.getResponse(SELECT_FAIL, "没有此用户");
        }
        int user_id = user.getId();
        String key = this.mosaicKey(order_id, user_id);
        String userUnpacking = userUnpackingRedis.getUserUnpacking(key);
        if (userUnpacking==null){
            if (type==0)return APIUtil.getResponse(SUCCESS, false);
            else {
                userUnpackingRedis.setUserUnpacking(key,"true");
                return APIUtil.getResponse(SUCCESS, "");
            }
        }else
            return APIUtil.getResponse(SUCCESS, true);
    }
    /**拼接redis键值对*/
    private String mosaicKey(String order_id, int user_id){
        StringBuffer str = new StringBuffer();
        str.append(order_id);
        str.append(DELIMITER);
        str.append(user_id);
        return str.toString();
    }
}
