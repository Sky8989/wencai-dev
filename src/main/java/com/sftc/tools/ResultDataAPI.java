package com.sftc.tools;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description:
 * @date 17/4/1
 * @Time 下午10:47
 */
public class ResultDataAPI {

    private String state = "00001";
    private String message = "success";
    private Object data;

    public ResultDataAPI(String state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public ResultDataAPI(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public Object getData() {
        return data;
    }
}
