package com.sftc.tools.constant;

import com.sftc.tools.sf.SFTokenHelper;

import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentDev;
import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentProduct;
import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentStage;
import static com.sftc.tools.constant.SFConstant.SFEnvironment.SFEnvironmentDev;
import static com.sftc.tools.constant.SFConstant.SFEnvironment.SFEnvironmentStage;
import static com.sftc.web.config.SFConfig.*;

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
        SFEnvironmentProduct    // 生产环境
    }

    private static SFEnvironment environment = SFEnvironmentDev; // 默认开发环境

    /**
     * 同城根地址
     */
    private static String SF_SAME_DOMAIN = (environment == SFEnvironmentDev ? SF_SAME_DOMAIN_DEV : (environment == SFEnvironmentStage ? SF_SAME_DOMAIN_STAGE : SF_SAME_DOMAIN_PRODUCT));


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
    public static String SF_DEVICE_URL = SF_SAME_DOMAIN + "devices";
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
     * 计价
     */
    public static String SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
    /**
     * 优惠券获取
     */
    public static String SF_COUPON_LIST_URL = SF_SAME_DOMAIN + "coupons/by_user/{user_uuid}?status={status}&limit={limit}&offset={offset}";
    /**
     * 优惠券兑换
     */
    public static String SF_COUPON_EXCHANGE_API = SF_SAME_DOMAIN + "coupons?promo_code={promo_code}";
    /**
     * 通用订单请求地址
     */
    public static String SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
    /**
     * 获取常量
     */
    public static String SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
    /**
     * 订单状态同步SF_NATION_ORDER_URL
     */
    public static String SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";


    /////////////// 大网用户相关 ///////////////

    /**
     * 获取token
     */
    public static final String SF_TOKEN_URL = SF_NATION_DOMAIN_PRODUCT + "token";


    /////////////// 大网订单相关 ///////////////

    /**
     * 大网订单根地址
     */
    private static final String SF_NATION_ORDER_URL = SF_NATION_DOMAIN_PRODUCT + "api/sforderservice/";
    /**
     * 创建订单
     */
    public static final String SF_CREATEORDER_URL = SF_NATION_ORDER_URL + "ordercreate";
//    /**
//     * 计价
//     */
//    public static final String SF_COUNT_PRICE = SF_NATION_ORDER_URL + "OrderFreightQuery";
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
        SF_DEVICE_URL = SF_SAME_DOMAIN + "devices";
        SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
        SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";
        SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
        SF_COUPON_LIST_URL = SF_SAME_DOMAIN + "coupons/by_user/{user_uuid}?status={status}&limit={limit}&offset={offset}";
        SF_COUPON_EXCHANGE_API = SF_SAME_DOMAIN + "coupons?promo_code={promo_code}";
        SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
        SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
        SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";

        // 设置环境的时候，除了替换SFAPI的URL，还要更换公共token和公共uuid。
        // 公共uuid可以先写死，因为数据库一般情况不会删档，uuid也是固定的，在设置环境的时候进行替换。
        // 目前使用的都是Bingo的uuid，开发环境编号为10093，生产环境编号为10136。
        SFTokenHelper.COMMON_UUID = environment == SFEnvironmentDev ? "2c9a85895d82ebe7015d8d4c6cc11df6" : "2c9280825bf24c1f015bf74d0ddf615d";
        // TODO：硬编码替换token，是不正确的，token过了一段时间就会失效。需要使用其它方案，暂时使用API来设置公共token，每次项目部署后都应该手动调用API进行设置。
        SFTokenHelper.COMMON_ACCESSTOKEN = environment == SFEnvironmentDev ? "EyMivbd44I124lcddrBG" : "tJN5WHBVVTia2JsC0Fjh";
    }

    public static String getSfSameDomain() {
        return SF_SAME_DOMAIN;
    }
}
