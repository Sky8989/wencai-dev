package com.sftc.tools.constant;

import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentDev;
import static com.sftc.tools.constant.DKConstant.DKEnvironment.DKEnvironmentStage;
import static com.sftc.tools.constant.ThirdPartyConstant.QN_DOMAIN;

/**
 * 蛋壳系统常量
 */
public class DKConstant {

    /**
     * 开发环境
     */
    public enum DKEnvironment {
        DKEnvironmentDev,       // 开发环境
        DKEnvironmentStage,     // 测试环境
        DKEnvironmentProduct;   // 生产环境
    }

    /////////////// 顺丰API根域名 ///////////////

    private static final String DK_DOMAIN_DEV = "https://sftc.dankal.cn/";            // dev
    private static final String DK_DOMAIN_STAGE = "https://test.sf-api.dankal.cn/";   // test
    private static final String DK_DOMAIN_PRODUCT = "https://api-wxc.sf-rush.com/";   // product

    private static DKEnvironment environment = DKEnvironmentDev;

    /**
     * 蛋壳域名
     */
    private static String DK_DOMIN = environment == DKEnvironmentDev ? DK_DOMAIN_DEV : (environment == DKEnvironmentStage ? DK_DOMAIN_STAGE : DK_DOMAIN_PRODUCT);

    /**
     * 默认头像占位图
     */
    public static final String DK_USER_AVATAR_DEFAULT = QN_DOMAIN + "anim_package.png";

    /**
     * phantomJS 页面地址
     */
    public static String DK_PHANTOMJS_WEB_URL = DK_DOMIN + "web/index.html?order_id=";


    public static void setEnvironment(DKEnvironment environment) {
        DKConstant.environment = environment;
        DK_DOMIN = environment == DKEnvironmentDev ? DK_DOMAIN_DEV : (environment == DKEnvironmentStage ? DK_DOMAIN_STAGE : DK_DOMAIN_PRODUCT);
        DK_PHANTOMJS_WEB_URL = DK_DOMIN + "web/index.html?order_id=";
    }
}
