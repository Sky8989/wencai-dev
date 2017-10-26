package com.sftc.tools.token;


import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserInviteMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    private static TokenUtils tokenUtils = null;

    private TokenUtils(){

    }

    public static final TokenUtils  getInstance(){
        if(tokenUtils == null){
            tokenUtils =  new TokenUtils();
            return tokenUtils;
        }
        return tokenUtils;
    }

    public  String getToken() {
        ServletRequestAttributes req = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = req.getRequest();
        String localToken = request.getHeader("token");

        return localToken;
    }

    public Integer getUserId(TokenMapper tokenMapper){
       return tokenMapper.getUserIdByLocalToken(getToken());
    }
}
