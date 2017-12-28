package com.sftc.tools.constant;

import com.sftc.tools.sf.SfTokenHelper;

import static com.sftc.tools.constant.DkConstant.DkEnvironment.*;
import static com.sftc.tools.constant.SfConstant.SFEnvironment.SfEnvironmentDev;
import static com.sftc.tools.constant.SfConstant.SFEnvironment.SfEnvironmentStage;
import static com.sftc.web.config.SfConfig.*;


/**
 * 顺丰API常量
 *
 * @author Administrator
 */
public class SfConstant {

    /**
     * 开发环境
     */
    public enum SFEnvironment {
        // 开发环境
        SfEnvironmentDev,
        // 开发环境
        SfEnvironmentStage,
        // 开发环境
        SfEnvironmentProduct
    }

    /**
     * 开发环境
     */
    private static SFEnvironment environment = SfEnvironmentDev;

    /**
     * 同城根地址
     */
    private static String SF_SAME_DOMAIN = (environment == SfEnvironmentDev ? SF_SAME_DOMAIN_DEV
            : (environment == SfEnvironmentStage ? SF_SAME_DOMAIN_STAGE : SF_SAME_DOMAIN_PRODUCT));

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

    /**
     * 用户钱包
     */
    public static String SF_WALLET = SF_SAME_DOMAIN + "wallets/by_user/";

    /**
     * 用户钱包
     */
    public static String SF_BALANCER_DETAILED = SF_SAME_DOMAIN + "transactions/by_user/";


    /////////////// 同城订单相关 ///////////////

    /**
     * 计价
     */
    public static String SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
    /**
     * 多包裹计价
     */
    public static String SF_Multiple_QUOTES_URL = SF_QUOTES_URL + "/in_group";
    /**
     * 多包裹支付
     */
    public static String SF_Multiple_PAY_URL = SF_SAME_DOMAIN + "request_groups";
    /**
     * 优惠券获取
     */
    public static String SF_COUPON_LIST_URL = SF_SAME_DOMAIN
            + "coupons/by_user/{user_uuid}?status={status}&limit={limit}&offset={offset}";
    /**
     * 优惠券兑换
     */
    public static String SF_COUPON_EXCHANGE_API = SF_SAME_DOMAIN + "coupons?promo_code={promo_code}";
    /**
     * 通用订单请求地址
     */
    public static String SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
    /**
     * 判断是否可同城
     */
    public static String SF_DETERMINE_URL = SF_SAME_DOMAIN + "/requests/coordinates/valid";
    /**
     * 多包裹订单请求地址
     */
    public static String SF_Multiple_REQUEST_URL = SF_REQUEST_URL + "/in_group";
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
    // /**
    // * 计价
    // */
    // public static final String SF_COUNT_PRICE = SF_NATION_ORDER_URL +
    // "OrderFreightQuery";
    /**
     * 路由地址
     */
    public static final String SF_ORDERROUTE_URL = SF_NATION_ORDER_URL + "OrderRouteQuery?orderid=";

    public static void setEnvironment(SFEnvironment environment) {
        SfConstant.environment = environment;

        switch (environment) {
            case SfEnvironmentDev:
                DkConstant.setEnvironment(DkEnvironmentDev);
                break;
            case SfEnvironmentStage:
                DkConstant.setEnvironment(DkEnvironmentStage);
                break;
            case SfEnvironmentProduct:
                DkConstant.setEnvironment(DkEnvironmentProduct);
                break;
            default:
                break;
        }

        SF_SAME_DOMAIN = (environment == SfEnvironmentDev ? SF_SAME_DOMAIN_DEV
                : (environment == SfEnvironmentStage ? SF_SAME_DOMAIN_STAGE : SF_SAME_DOMAIN_PRODUCT));
        SF_TAKE_MESSAGE_URL = SF_SAME_DOMAIN + "messages";
        SF_GET_TOKEN = SF_SAME_DOMAIN + "merchants/me/token";
        SF_DEVICE_URL = SF_SAME_DOMAIN + "devices";
        SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
        SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";
        SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
        SF_COUPON_LIST_URL = SF_SAME_DOMAIN
                + "coupons/by_user/{user_uuid}?status={status}&limit={limit}&offset={offset}";
        SF_COUPON_EXCHANGE_API = SF_SAME_DOMAIN + "coupons?promo_code={promo_code}";
        SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
        SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";
        SF_ORDER_SYNC_URL = SF_SAME_DOMAIN + "requests/{uuid}/status?batch=true";

        // 设置环境的时候，除了替换SFAPI的URL，还要更换公共token和公共uuid。
        // 公共uuid可以先写死，因为数据库一般情况不会删档，uuid也是固定的，在设置环境的时候进行替换。
        // 目前使用的都是Bingo的uuid，开发环境编号为10093，生产环境编号为10136。
        SfTokenHelper.COMMON_UUID = environment == SfEnvironmentDev ? "2c9a85895d82ebe7015d8d4c6cc11df6"
                : "2c9280825bf24c1f015bf74d0ddf615d";
        // TODO：硬编码替换token，是不正确的，token过了一段时间就会失效。需要使用其它方案，暂时使用API来设置公共token，每次项目部署后都应该手动调用API进行设置。
        SfTokenHelper.COMMON_ACCESSTOKEN = environment == SfEnvironmentDev ? "EyMivbd44I124lcddrBG"
                : "tJN5WHBVVTia2JsC0Fjh";
    }

    public static String getSfSameDomain() {
        return SF_SAME_DOMAIN;
    }
}
