package com.sftc.tools;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.tools
 * @Description:
 * @date 17/4/5
 * @Time 下午8:36
 */
public enum ApiResponseEnum {

    SUCCESS("00001", "success"),
    FAIL("00002", "fail");

    private String state;
    private String message;

    ApiResponseEnum(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMsg(String code) {
        for (ApiResponseEnum responseEnum : ApiResponseEnum.values()) {
            if (responseEnum.getState().equals(code)) {
                return responseEnum.getMessage();
            }
        }
        return null;
    }
}
