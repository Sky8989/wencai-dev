package com.sftc.tools.api;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.tools.api
 * @Description: API封装工具类
 * @date 2017/4/6
 * @Time 下午5:46
 */
public class APIUtil {

    private APIResponse response;

    public static APIResponse getResponse(APIStatus apiStatus, Object obj) {
        APIResponse apiResponse = APIResponse.getInstance();
        apiResponse.setState(apiStatus.getState());
        apiResponse.setMessage(apiStatus.getMessage());
        apiResponse.setResult(obj);
        return apiResponse;
    }
}
