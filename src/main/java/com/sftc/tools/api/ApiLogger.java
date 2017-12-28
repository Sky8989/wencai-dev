package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.vo.BaseVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bingo
 */
@Component
@Aspect
public class ApiLogger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.sftc.web.service..*(..))")
    public void apiPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("apiPointcut()")
    public void before(JoinPoint joinPoint) {
        Object[] obj = joinPoint.getArgs();
        // 符合API设计规范
        if (obj.length > 0 && obj.length == 1 && obj[0].getClass().getSuperclass().equals(BaseVO.class)) {
            BaseVO baseVO = (BaseVO) obj[0];
            // Json params
            if (baseVO != null) {
                String requestObj = new Gson().toJson(baseVO);
                logger.info("[Params] " + requestObj);
            }
        } else if (obj.length == 0) {
            logger.info("[Params] null");
        } else {
            ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //项目中的定时器在容器加载之前，需要判断
            if (res != null) {
                HttpServletRequest request = res.getRequest();
                String url = request.getQueryString();
                logger.info("[Params] " + "?" + url);
            }
        }
    }

    /**
     * 后置返回通知
     */
    @AfterReturning(pointcut = "apiPointcut()", argNames = "joinPoint, response", returning = "response")
    public void afterReturn(JoinPoint joinPoint, ApiResponse response) {
        logger.info(joinPoint + " Response: " + new Gson().toJson(response) + "\n");
    }

    /**
     * 抛出异常后通知
     */
    @AfterThrowing(pointcut = "apiPointcut()", throwing = "ex")
    public void afterThrow(JoinPoint joinPoint, Exception ex) {
        logger.error(joinPoint + " Exception: " + ex.getMessage());
    }
}