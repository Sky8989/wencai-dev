package com.sftc.tools.api;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Aspect
public class APILogger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.sftc.web.service..*(..))")
    public void apiPointcut() {
    }

    // 前置通知
    @Before("apiPointcut()")
    public void before(JoinPoint joinPoint) {
        Object obj[] = joinPoint.getArgs();
        if (obj.length > 0 && obj[0].getClass().equals(APIRequest.class)) { // 符合API设计规范
            APIRequest request = (APIRequest) obj[0];
            Set set;
            if (request.getRequestParam() != null) { // Json params
                Map map = JSONObject.fromObject(new Gson().toJson(request.getRequestParam()));
                set = map.entrySet();
            } else { // Form params
                set = request.getParams().entrySet();
            }
            Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
            for (Map.Entry entry : entries) {
                logger.info("[Params] " + entry.getKey() + ":" + entry.getValue());
            }
        } else {
            logger.info("[Params] null");
        }
    }

    // 后置返回通知
    @AfterReturning(pointcut = "apiPointcut()", argNames = "joinPoint, response", returning = "response")
    public void afterReturn(JoinPoint joinPoint, APIResponse response) {
        logger.info(joinPoint + " Response: " + new Gson().toJson(response) + "\n");
    }

    // 抛出异常后通知
    @AfterThrowing(pointcut = "apiPointcut()", throwing = "ex")
    public void afterThrow(JoinPoint joinPoint, Exception ex) {
        logger.error(joinPoint + " Exception: " + ex.getMessage());
    }
}
