package com.sftc.tools.api;

import static com.sftc.tools.api.APIStatus.*;

public class APIUtil {

    /**
     * 通过接口状态枚举和响应结果对象，构造APIResponse
     *
     * @param apiStatus 接口状态枚举
     * @param obj       响应结果对象
     * @return APIResponse
     */
    public static APIResponse getResponse(APIStatus apiStatus, Object obj) {
        APIResponse apiResponse = apiResponse();
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
    public static APIResponse paramErrorResponse(String message) {
        APIResponse noResponse = errorResponse(message);
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
    public static APIResponse submitErrorResponse(String message, Object errorObj) {
        APIResponse apiResponse = errorResponse(message);
        apiResponse.setState(SUBMIT_FAIL.getState());
        apiResponse.setResult(errorObj);
        return apiResponse;
    }

    /**
     * 查询异常
     *
     * @param message  异常信息
     * @param errorObj 异常结果对象
     * @return APIResponse
     */
    public static APIResponse selectErrorResponse(String message, Object errorObj) {
        APIResponse apiResponse = errorResponse(message);
        apiResponse.setState(SELECT_FAIL.getState());
        apiResponse.setResult(errorObj);
        return apiResponse;
    }

    // 初始化单例参数
    private static APIResponse apiResponse() {
        APIResponse apiResponse = APIResponse.getInstance();
        apiResponse.setState(SUCCESS.getState());
        apiResponse.setMessage(SUCCESS.getMessage());
        apiResponse.setResult(null);
        return apiResponse;
    }

    // 初始化异常单例
    private static APIResponse errorResponse(String message) {
        APIResponse apiResponse = apiResponse();
        apiResponse.setMessage(message);
        return apiResponse;
    }
}
