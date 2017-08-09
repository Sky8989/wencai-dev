package com.sftc.tools.common;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import net.sf.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的异常处理
 * Created by huxingyue on 2017/8/9.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    // 通用异常的处理，返回500
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<APIResponse> handleException(Exception ex) {
        return ControllerHelper.responseEntityBuilder(APIUtil.logicErrorResponse("Dankal_Server_Error", ex.getStackTrace()));
    }
}
