package com.sftc.tools.api;

public enum APIStatus {

    SUCCESS("00001", "success"),
    USER_FAIL("00002", "用户名或密码错误"),
    USER_NOT_EXIST("00003", "用户名不存在"),

    SELECT_FAIL("00002", "查询失败"),
    SUBMIT_FAIL("00004", "提交失败"),

    ORDER_PAY_FAIL("00005", "订单支付失败"),
    ORDER_PACKAGE_COUNT_PULL("00006", "包裹已分发完"),
    ORDER_NOT_FOUND("00007", "没有该订单"),
    COURIER_NOT_FOUND("00008", "没有该订单"),
    WECHAT_ERR("", ""),
    CANCEL_ORDER_FALT("00009", "falt"),
    GET_MESSAGE("000010", "VALIDATION_ERROR"),
    REGISTER_FAIL("000011", "验证码错误或已失效"),
    REGISTER_SUCCESS("000012", "注册成功"),
    ERROR("000013", "账号或密码错误"),
    TOKEN_SUCCESS("000014", "获取token成功"),
    LOGIN_ERROR("000015", "密码或验证码错误"),
    AUTHORIZATION_FAIL("000016", "鉴权失败"),
    VALIDATION_ERROR("000017", "请输入正确的手机号码"),
    GIFT_CARD_NOT_FOUND("00009", "没有该礼卡"),
    QUOTE_FAIL("000018", "计价失败"),
    ADDRESS_FAIL("0000019", "新增地址失败"),
    PARAMETER_FAIL("0000020", "参数传递错误"),
    LOGIN_FAIL("0000021", "获取个人信息失败"),
    EVALUATE_FALT("0000022", "评价失败"),
    CONSTANT_FALT("0000023", "获取常量失败"),
    ORDERROUT_FALT("0000024", "参数错误或路由信息为空");

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
