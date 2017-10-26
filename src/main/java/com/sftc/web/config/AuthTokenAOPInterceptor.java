package com.sftc.web.config;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//全局token验证
public class AuthTokenAOPInterceptor {
    @Resource
    private TokenMapper tokenMapper;

    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取 request response 对象
        ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = res.getRequest();
        HttpServletResponse response = res.getResponse();
        String token = request.getHeader("token");
        //根据请求的url判断，如果是同城转大网，则需要走临时token验证逻辑 getRequestURL()+"?"
        // + request.getQueryString()).equals("http://localhost:8080/sftc/order/transform?uuid=xxxxxxxxxxx")
        if((request.getRequestURI().equals("/sftc/order/transform"))){
           Token transformToken = tokenMapper.getTokenById(2188);   //2188用户存放临时token
           if(token.equals(transformToken.getLocal_token())){
               token = transformToken.getLocal_token();
           }else {
               String reason = "数据库中找不到此token";
               return APIUtil.selectErrorResponse("temporary token is not exist",reason);
           }
        }
        //获取当前执行的方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //判断当前执行的方法是否存在自定义的注解
        if (methodSignature.getMethod().isAnnotationPresent(IgnoreToken.class)) {
            //只需要过滤登录即可，所以考虑在登录方法上加上注解，存在注解的就不验证token，不存在注解的需要验证
           return proceedingJoinPoint.proceed();
        }else {
            if (token != null && !token.equals("")) {
                User user = tokenMapper.tokenInterceptor(token);
                APIResponse error = null;
                if(user!=null && user.getId() == 2188){//如果token为临时token，则进入临时token验证逻辑
                    error = transformTokenCheck(token); //临时token验证
                    if (error != null) {//临时token失效则需要重新获取
                        String reason = "token已失效，请重新获取";
                        return APIUtil.selectErrorResponse("temporary token is unavailable Please take again", reason);
                    }
                }else {//校验用户
                    error = authTokenCheck(token);
                    if (error != null) {
                        String reason = "数据库中找不到此token";
                        return APIUtil.selectErrorResponse("网络繁忙，请稍后", reason);
                    }
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
                handlerInterceptorAdapter.preHandle(request,response,proceedingJoinPoint);
                String reason = "token验证失败";
                return APIUtil.selectErrorResponse("Authtoken is unavailable", reason);
            }
        }
    }

    //普通全局token验证方法
    private APIResponse authTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        if (user != null) { //此验证只需要找到用户即视为通过
            return null;
        } else {
            return APIUtil.selectErrorResponse("token验证失败", null);
        }
    }

    //同城转大网时的临时token验证方法
    private APIResponse transformTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        if (user != null) { //临时token验证需要判断是否已经过了有效时间
            Token token1 = tokenMapper.getTokenById(user.getId());
            long dataTime = System.currentTimeMillis();
            long tempTime = Long.parseLong(token1.getGmt_expiry());
            if (dataTime < tempTime || dataTime == tempTime) {
                return null;
            } else {
                return APIUtil.selectErrorResponse("token已失效，请重新获取", null);
            }
        } else {
            return APIUtil.selectErrorResponse("token验证失败", null);
        }
    }
}

