package com.sftc.tools.token;
import com.sftc.web.dao.mybatis.TokenMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
/**
 * 
 * @author wencai
 * 2017-10-27
 */
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
