package com.sftc.tools.token;

import com.sftc.tools.spring.SpringContextHolder;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
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
     * 获取用户access_token
     *
     * @return access_token
     */
    public String getAccess_token() {
        Token token = tokenMapper.getTokenById(getUserId());
        if (token != null)
            return token.getAccess_token();
        else
            return null;
    }

    /**
     * 获取用户uuid
     *
     * @return uuid
     */
    public String getUserUUID() {
        User user = tokenMapper.tokenInterceptor(getToken());
        if (user != null)
            return user.getUuid();
        else
            return null;
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
