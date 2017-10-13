package com.sftc.web.config;

import com.google.gson.Gson;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class AuthTokenAOPInterceptor {
    @Resource
    private TokenMapper tokenMapper;

    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = res.getRequest();
        HttpServletResponse response = res.getResponse();
        String token = request.getHeader("token");
        //获取当前执行的方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //判断当前执行的方法是否存在自定义的注解
        if (methodSignature.getMethod().isAnnotationPresent(AuthToken.class)) {
            //只需要过滤登录就可，所以考虑在登录方法上加上注解，存在注解的就不验证token
           return proceedingJoinPoint.proceed();
        }else {
            if (token != null && !token.equals("")) {
                APIResponse error = authTokenCheck(token);//校验用户
                if (error != null) {
                    return APIUtil.selectErrorResponse(error+"",null);
                }
            } else {
                return APIUtil.selectErrorResponse("token验证失败", null);
            }
            return null;
        }
    }

//    private void error(HttpServletResponse response, APIResponse error) {
//        response.reset();
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        OutputStreamWriter ow = null;
//        try {
//            ServletOutputStream out = response.getOutputStream();
//            ow = new OutputStreamWriter(out, "UTF-8");
//            ow.write(new Gson().toJson(error));
//        } catch (IOException e) {
//            e.fillInStackTrace();
//        } finally {
//            try {
//                ow.flush();
//                ow.close();
//            } catch (IOException e) {
//                e.fillInStackTrace();
//            }
//        }
//    }

    private APIResponse authTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        if (user != null) {
            return null;
        } else {
            return APIUtil.selectErrorResponse("token验证失败", null);
        }
    }
}

