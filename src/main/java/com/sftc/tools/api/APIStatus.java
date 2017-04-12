package com.sftc.tools.api;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.tools.api
 * @Description: 接口的状态码以及错误信息
 * @date 2017/4/6
 * @Time 下午5:46
 */
public enum APIStatus {

    SUCCESS("00001", "success"),
    FAIL("00002", "fail"),

    USER_FAIL("00002", "用户名或密码错误"),
    USER_NOT_EXIST("00003", "用户名不存在");

    private String state;
    private String message;

    APIStatus(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
