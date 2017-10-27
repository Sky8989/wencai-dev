package com.sftc.tools.token;

import com.sftc.tools.spring.SpringContextHolder;
import com.sftc.web.dao.mybatis.TokenMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * token工具类
 */
public class TokenUtils {

    private TokenMapper tokenMapper;

    private TokenUtils() {

    }

    /**
     * 获取token工具单例对象
     *
     * @return TokenUtils单例对象
     */
    public static TokenUtils getInstance() {
        TokenUtils tokenUtils = TokenUtilsHolder.instance;
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
     *
     * @return user_id
     */
    public Integer getUserId() {
        return tokenMapper.getUserIdByLocalToken(getToken());
    }

    /**
     * 单例对象静态类
     */
    private static class TokenUtilsHolder {
        private final static TokenUtils instance = new TokenUtils();
    }
}