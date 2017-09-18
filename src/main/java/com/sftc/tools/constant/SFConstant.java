package com.sftc.tools.constant;

import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentDev;
import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentProduct;
import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentStage;
import static com.sftc.tools.constant.SFConstant.SFEnvironment.SFEnvironmentDev;
import static com.sftc.tools.constant.SFConstant.SFEnvironment.SFEnvironmentStage;

/**
 * 顺丰API常量
 */
public class SFConstant {

    /**
     * 开发环境
     */
    public enum SFEnvironment {
        SFEnvironmentDev,       // 开发环境
        SFEnvironmentStage,     // 测试环境
        SFEnvironmentProduct;   // 生产环境
    }

    private static SFEnvironment environment = SFEnvironmentDev;

    /////////////// 顺丰API根域名 ///////////////

    private static final String SF_SAME_DOMAIN_DEV = "http://api-dev.sf-rush.com/";     // dev
    private static final String SF_SAME_DOMAIN_STAGE = "http://api-stage.sf-rush.com/"; // test
//    private static final String SF_SAME_DOMAIN_PRODUCT = "http://api.sf-rush.com/";   // product
    private static final String SF_SAME_DOMAIN_PRODUCT = "http://192.168.0.31:8080/";   // product

    /**
     * 同城根地址
     */
    private static String SF_SAME_DOMAIN = (environment == SFEnvironmentDev ? SF_SAME_DOMAIN_DEV : (environment == SFEnvironmentStage ? SF_SAME_DOMAIN_STAGE : SF_SAME_DOMAIN_PRODUCT));

    /**
     * 大网根地址
     */
    private static final String SF_NATION_DOMAIN = "http://api-c.sf-rush.com/";         // 生产环境
//    private static final String SF_NATION_DOMAIN = "http://api-c-test.sf-rush.com/";    // dev环境


    /////////////// 同城用户相关 ///////////////

    /**
     * 获取验证码
     */
    public static String SF_TAKE_MESSAGE_URL = SF_SAME_DOMAIN + "messages";

    /**
     * 获取图片验证码
     */
    public static String SF_TAKE_CAPTCHAS_MESSAGE_URL = SF_SAME_DOMAIN + "captchas";


    /**
     * 获取token
     */
    public static String SF_GET_TOKEN = SF_SAME_DOMAIN + "merchants/me/token";
    /**
     * 用户注册
     */
    public static String SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
    /**
     * 用户登录
     */
    public static String SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";


    /////////////// 同城订单相关 ///////////////

    /**
     * 优惠券
     */
    public static String SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
    /**
     * 通用订单请求地址
     */
    public static String SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
    /**
     * 获取常量
     */
    public static String SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
    /**
     * 订单状态同步
     */
    public static String SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";


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


    public static void setEnvironment(SFEnvironment environment) {
        SFConstant.environment = environment;

        switch (environment) {
            case SFEnvironmentDev:
                DKConstant.setEnvironment(DKEnvironmentDev);
                break;
            case SFEnvironmentStage:
                DKConstant.setEnvironment(DKEnvironmentStage);
                break;
            case SFEnvironmentProduct:
                DKConstant.setEnvironment(DKEnvironmentProduct);
                break;
        }

        SF_SAME_DOMAIN = (environment == SFEnvironmentDev ? SF_SAME_DOMAIN_DEV : (environment == SFEnvironmentStage ? SF_SAME_DOMAIN_STAGE : SF_SAME_DOMAIN_PRODUCT));
        SF_TAKE_MESSAGE_URL = SF_SAME_DOMAIN + "messages";
        SF_GET_TOKEN = SF_SAME_DOMAIN + "merchants/me/token";
        SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
        SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";
        SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
        SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
        SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
        SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";
    }

    public static String getSfSameDomain() {
        return SF_SAME_DOMAIN;
    }
}
