package com.sftc.tools.api;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.other
 * @Description: API模型类
 * @date 2017/4/6
 * @Time 下午4:54
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class APIResponse {

    private String state;
    private String message;
    private Object data;

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public Object getData() {
        return data;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private static class APIResponseHolder {
        private final static APIResponse INSTANCE = new APIResponse();
    }

    public static APIResponse getInstance() {
        return APIResponseHolder.INSTANCE;
    }

    private APIResponse() {}
}
