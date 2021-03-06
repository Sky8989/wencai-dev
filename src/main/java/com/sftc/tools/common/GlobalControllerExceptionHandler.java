package com.sftc.tools.common;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Arrays;

/**
 * 全局的异常处理
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    // 对参数错误进行处理
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(org.springframework.web.bind.ServletRequestBindingException.class)
    @ResponseBody
    public ResponseEntity<APIResponse> handleException2(Exception ex, ServletWebRequest ServletWebRequest) {
        logger.error(" Exception: " + ex.getMessage());
        logger.error(" StackTrace: " + Arrays.toString(ex.getStackTrace()));
        logger.error(" ContextPath: " + ServletWebRequest.getContextPath() + ServletWebRequest.getRequest().getRequestURI());
        return ControllerHelper.responseEntityBuilder(APIUtil.paramErrorResponse("Parameter missing"));
    }

    //  通用异常的处理，返回500
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<APIResponse> handleException(Exception ex, ServletWebRequest ServletWebRequest) {
        // 结构化异常信息
        JSONObject resultOBJ = new JSONObject();
        resultOBJ.put("Message", ex.getMessage());
        resultOBJ.put("LocalizedMessage", ex.getLocalizedMessage());
        resultOBJ.put("toString", ex.toString());
        resultOBJ.put("Cause", ex.getCause());
        resultOBJ.put("StackTrace", ex.getStackTrace());
        // 记录异常信息
        logger.error(" ContextPath: " + ServletWebRequest.getContextPath() + ServletWebRequest.getRequest().getRequestURI());
        logger.error(" ParameterMap: ");
        ServletWebRequest.getParameterMap().forEach((k, v) -> System.out.print(k + ":" + Arrays.toString(v)));
        logger.error(" Exception: " + ex.getMessage());
        logger.error(" StackTrace: " + Arrays.toString(ex.getStackTrace()));
        return ControllerHelper.responseEntityBuilder(APIUtil.logicErrorResponse("系统异常，请您稍后再尝试"
                , resultOBJ));
    }
}
