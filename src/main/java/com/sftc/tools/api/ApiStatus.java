package com.sftc.tools.api;

/**
 * @author Administrator
 */
public enum ApiStatus {
    /**
     * api状态处理
     */
    SUCCESS(200, "success"),
    PARAM_ERROR(400, "参数异常"),
    SELECT_FAIL(401, "查询失败"),
    SUBMIT_FAIL(402, "提交失败"),
    LOGIC_ERROR(500, "系统异常");

    private Integer state;
    private String message;

    ApiStatus(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    public Integer getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
