package com.sftc.web.config;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.entity.User;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局token验证
 */
@Component
@Aspect
public class AuthTokenAOPInterceptor {
    @Resource
    private TokenMapper tokenMapper;

    @Pointcut("execution(* com.sftc.web.controller.*Controller.*(..))")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取 request response 对象
        ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = res.getRequest();
        HttpServletResponse response = res.getResponse();
        String token = request.getHeader("token");
        //获取当前执行的方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //判断当前执行的方法是否存在自定义的注解
        if (methodSignature.getMethod().isAnnotationPresent(IgnoreToken.class)) {
            //只需要过滤登录即可，所以考虑在登录方法上加上注解，存在注解的就不验证token，不存在注解的需要验证
            return proceedingJoinPoint.proceed();
        } else {
            //map构建返回对象
            Map<String, String> map = new HashMap<>(1, 1);
            //校验用户
            if (StringUtils.isNotBlank(token)) {
                ApiResponse error = authTokenCheck(token);
                if (error != null) {
                    map.put("reason", "数据库中找不到此token");
                    res.getResponse().setStatus(HttpStatus.UNAUTHORIZED.value());
                    error.setError(map);
                    return error;
                }

                //验证成功返回null
                return proceedingJoinPoint.proceed();
            } else {
                //构建一个 springMVC 拦截器
                HandlerInterceptorAdapter handlerInterceptorAdapter = new HandlerInterceptorAdapter() {
                    @Override
                    //预处理，返回false时不会下传，往回传递，被拦截的方法不会执行
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        return false;
                    }

                };
                handlerInterceptorAdapter.preHandle(request, response, proceedingJoinPoint);
                map.put("reason", "AuthToken is unavailable");

                ApiResponse result = new ApiResponse();
                result.setState(null);
                result.setMessage("token验证失败");
                result.setError(map);
                res.getResponse().setStatus(HttpStatus.UNAUTHORIZED.value());
                return result;
            }
        }
    }

    /**
     * 普通全局token验证方法
     */
    private ApiResponse authTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        //此验证只需要找到用户即视为通过
        if (user != null) {
            return null;
        } else {
            ApiResponse result = new ApiResponse();
            result.setState(null);
            result.setMessage("token验证失败");
            return result;
        }
    }
}

