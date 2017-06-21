package com.sftc.tools.api;

public class APIUtil {

    public static APIResponse getResponse(APIStatus apiStatus, Object obj) {
        APIResponse apiResponse = APIResponse.getInstance();
        apiResponse.setState(apiStatus.getState());
        apiResponse.setMessage(apiStatus.getMessage());
        apiResponse.setResult(obj);
        return apiResponse;
    }

    public static APIResponse errorResponse(String message) {
        APIResponse apiResponse = APIResponse.getInstance();
        apiResponse.setState("-1");
        apiResponse.setMessage(message);
        apiResponse.setResult(null);
        return apiResponse;
    }
}
