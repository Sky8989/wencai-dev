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
    USER_FAIL("00002", "用户名或密码错误"),
    USER_NOT_EXIST("00003", "用户名不存在"),

    ORDER_SUBMIT_FAIL("00004", "提交失败"),
    ORDER_PAY_FAIL("00005", "订单支付失败"),
    ORDER_PACKAGE_COUNT_PULL("00006", "包裹已分发完"),

    WECHAT_ERR("", "");

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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setState(String state) {
        this.state = state;
    }
}
