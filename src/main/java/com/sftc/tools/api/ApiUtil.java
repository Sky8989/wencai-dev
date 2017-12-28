package com.sftc.tools.api;

import static com.sftc.tools.api.ApiStatus.*;

/**
 * @author Administrator
 */
public class ApiUtil {

    /**
     * 通过接口状态枚举和响应结果对象，构造APIResponse
     *
     * @param apiStatus 接口状态枚举
     * @param obj       响应结果对象
     * @return APIResponse
     */
    public static ApiResponse getResponse(ApiStatus apiStatus, Object obj) {
        ApiResponse apiResponse = apiResponse();
        apiResponse.setState(apiStatus.getState());
        apiResponse.setMessage(apiStatus.getMessage());
        apiResponse.setResult(obj);
        return apiResponse;
    }

    /**
     * 参数异常
     *
     * @param message 异常信息
     * @return APIResponse
     */
    public static ApiResponse paramErrorResponse(String message) {
        ApiResponse noResponse = errorResponse(message);
        noResponse.setState(PARAM_ERROR.getState());
        return noResponse;
    }

    /**
     * 提交异常
     *
     * @param message  异常信息
     * @param errorObj 异常结果对象
     * @return APIResponse
     */
    public static ApiResponse submitErrorResponse(String message, Object errorObj) {
        ApiResponse apiResponse = errorResponse(message);
        apiResponse.setState(SUBMIT_FAIL.getState());
        apiResponse.setError(errorObj);
        return apiResponse;
    }

    /**
     * 查询异常
     *
     * @param message  异常信息
     * @param errorObj 异常结果对象
     * @return APIResponse
     */
    public static ApiResponse selectErrorResponse(String message, Object errorObj) {
        ApiResponse apiResponse = errorResponse(message);
        apiResponse.setState(SELECT_FAIL.getState());
        apiResponse.setError(errorObj);
        return apiResponse;
    }

    /**
     * 系统逻辑处理异常
     *
     * @param message  异常信息
     * @param errorObj 异常结果对象
     * @return APIResponse
     */
    public static ApiResponse logicErrorResponse(String message, Object errorObj) {
        ApiResponse apiResponse = errorResponse(message);
        apiResponse.setState(LOGIC_ERROR.getState());
        apiResponse.setError(errorObj);
        return apiResponse;
    }

    /**
     * 初始化
     *
     * @return
     */
    private static ApiResponse apiResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setState(SUCCESS.getState());
        apiResponse.setMessage(SUCCESS.getMessage());
        apiResponse.setResult(null);
        return apiResponse;
    }

    /**
     * 初始化异常单例
     */
    private static ApiResponse errorResponse(String message) {
        ApiResponse apiResponse = apiResponse();
        apiResponse.setMessage(message);
        return apiResponse;
    }


    /**
     * http回调错误
     */
    public static ApiResponse error(Integer code, String msg) {
        ApiResponse result = new ApiResponse();
        result.setState(code);
        result.setMessage(msg);
        return result;
    }

    /**
     * http回调错误
     */
    public static ApiResponse error(Integer code, String msg, Object error) {
        ApiResponse result = new ApiResponse();
        result.setState(code);
        result.setMessage(msg);
        result.setError(error);
        return result;
    }
}
