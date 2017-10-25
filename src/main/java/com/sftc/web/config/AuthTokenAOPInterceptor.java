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
        if((request.getRequestURL()+"?" + request.getQueryString()).equals("https://sftc.dankal.cn/sftc/order/transform?uuid=xxxxxxxxxxx")){
           Token transformToken = tokenMapper.getTokenById(2188);
           token = transformToken.getLocal_token();
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
                if(user!=null && user.getId() == 2188){
                    error = transformTokenCheck(token);
                }else {//校验用户
                    error = authTokenCheck(token);
                }
                //验证成功返回null
                if (error != null) {
                    return APIUtil.selectErrorResponse("网络繁忙，请稍后", null);
                }
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
                return APIUtil.selectErrorResponse("token验证失败", null);
            }
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

    private APIResponse transformTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        if (user != null) {
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

