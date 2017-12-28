package com.sftc.tools.api;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 响应体切面
 * 后置通知修改httpstatus
 *
 * @author ： CatalpaFlat
 * @date ：Create in 18:42 2017/12/14
 */
@Component
@Aspect
public class ApiResponseAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 切面
     */
    private final String POINT_CUT = "execution(* com.sftc.web.controller..*(..))";

    @Pointcut(POINT_CUT)
    private void pointcut() {
    }

    @AfterReturning(value = POINT_CUT, returning = "apiResponse", argNames = "apiResponse")
    public void doAfterReturningAdvice2(ApiResponse apiResponse) {
        logger.info("apiResponse：" + apiResponse);
        Integer state = apiResponse.getState();
        if (state != null) {
            ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            res.getResponse().setStatus(state);
            apiResponse.setState(null);
        }

    }
}
