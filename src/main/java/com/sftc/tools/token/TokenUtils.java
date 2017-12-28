package com.sftc.tools.token;

import com.sftc.tools.spring.SpringContextHolder;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.entity.Token;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * token工具类
 *
 * @author Administrator
 */
//@Component
public class TokenUtils {

//    @Resource
    private TokenMapper tokenMapper;

    public TokenUtils() {

    }

    /**
     * 获取用户access_token
     *
     * @return access_token
     */
    public String getAccessToken() {
        Token token = tokenMapper.getTokenByUUId(getUserUUID());
        if (token != null) {
            return token.getAccess_token();
        }
        return null;
    }


    /**
     * 获取token工具单例对象
     *
     * @return TokenUtils单例对象
     */
    public static TokenUtils getInstance() {
        TokenUtils tokenUtils = TokenUtilsHolder.INSTANCE;
        tokenUtils.tokenMapper = SpringContextHolder.getBean(TokenMapper.class);

        return tokenUtils;
    }

    /**
     * 获取当前上下文中的用户token
     *
     * @return local_token
     */
    public String getToken() {
        ServletRequestAttributes req = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = req.getRequest();

        return request.getHeader("token");
    }

    /**
     * 根据token获取当前用户ID
     */
    public String getUserUUID() {
        return tokenMapper.getUserIdByLocalToken(getToken());
    }

    /**
     * 单例对象静态类
     */
    private static class TokenUtilsHolder {
        private final static TokenUtils INSTANCE = new TokenUtils();
    }


}
