package com.sftc.tools.constant;

/**
 * 顺丰API常量
 */
public class SFConstant {

    /////////////// 顺丰API根域名 ///////////////

    /**
     * 同城根地址
     */
    private static final String SF_SAME_DOMAIN = "http://api-dev.sf-rush.com/";
    /**
     * 大网根地址
     */
    private static final String SF_NATION_DOMAIN = "http://api-c.sf-rush.com/";         // 生产环境
//    private static final String SF_NATION_DOMAIN = "http://api-c-test.sf-rush.com/";    // dev环境


    /////////////// 同城用户相关 ///////////////

    /**
     * 获取验证码
     */
    public static final String SF_TAKE_MESSAGE_URL = SF_SAME_DOMAIN + "messages";
    /**
     * 获取token
     */
    public static final String SF_GET_TOKEN = SF_SAME_DOMAIN + "merchants/me/token";
    /**
     * 用户注册
     */
    public static final String SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
    /**
     * 用户登录
     */
    public static final String SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";


    /////////////// 同城订单相关 ///////////////

    /**
     * 优惠券
     */
    public static final String SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
    /**
     * 通用订单请求地址
     */
    public static final String SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
    /**
     * 获取常量
     */
    public static final String SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
    /**
     * 订单状态同步
     */
    public static final String SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";


    /////////////// 大网用户相关 ///////////////

    /**
     * 获取token
     */
    public static final String SF_TOKEN_URL = SF_NATION_DOMAIN + "token";


    /////////////// 大网订单相关 ///////////////

    /**
     * 大网订单根地址
     */
    private static final String SF_NATION_ORDER_URL = SF_NATION_DOMAIN + "api/sforderservice/";
    /**
     * 创建订单
     */
    public static final String SF_CREATEORDER_URL = SF_NATION_ORDER_URL + "ordercreate";
    /**
     * 计价
     */
    public static final String SF_COUNT_PRICE = SF_NATION_ORDER_URL + "OrderFreightQuery";
    /**
     * 路由地址
     */
    public static final String SF_ORDERROUTE_URL = SF_NATION_ORDER_URL + "OrderRouteQuery?orderid=";
}
