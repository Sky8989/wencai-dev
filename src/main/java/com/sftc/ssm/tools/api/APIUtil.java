package com.sftc.ssm.tools.api;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.tools.api
 * @Description:
 * @date 2017/4/6
 * @Time 下午5:46
 */
public class APIUtil {

    private APIResponse response;

    public static APIResponse getResponse(String status, String message, Object obj) {
        APIResponse apiResponse = APIResponse.getInstance();
        apiResponse.setState(status);
        apiResponse.setMessage(message);
        apiResponse.setData(obj);
        return apiResponse;
    }
}
