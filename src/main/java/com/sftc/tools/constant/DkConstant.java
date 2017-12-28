package com.sftc.tools.constant;

import static com.sftc.tools.constant.DkConstant.DkEnvironment.DkEnvironmentDev;
import static com.sftc.tools.constant.DkConstant.DkEnvironment.DkEnvironmentStage;
import static com.sftc.tools.constant.ThirdPartyConstant.QN_DOMAIN;

/**
 * 蛋壳系统常量
 *
 * @author Administrator
 */
public class DkConstant {

    /**
     * 开发环境
     */
    public enum DkEnvironment {
        // 开发环境
        DkEnvironmentDev,
        // 测试环境
        DkEnvironmentStage,
        // 生产环境
        DkEnvironmentProduct;
    }

    /////////////// 顺丰API根域名 ///////////////

    /**
     * dev
     */
    private static final String DK_DOMAIN_DEV = "https://sftc.dankal.cn/";
    /**
     * test
     */
    private static final String DK_DOMAIN_STAGE = "https://test.sf-api.dankal.cn/";
    /**
     * test
     */
    private static final String DK_DOMAIN_PRODUCT = "https://api-wxc.sf-rush.com/";

    private static DkEnvironment environment = DkEnvironmentDev;

    /**
     * 蛋壳域名
     */
    private static String DK_DOMIN = environment == DkEnvironmentDev ? DK_DOMAIN_DEV
            : (environment == DkEnvironmentStage ? DK_DOMAIN_STAGE : DK_DOMAIN_PRODUCT);

    /**
     * 默认头像占位图
     */
    public static final String DK_USER_AVATAR_DEFAULT = QN_DOMAIN + "anim_package.png";

    /**
     * phantomJS 页面地址
     */
    public static String DK_PHANTOMJS_WEB_URL = DK_DOMIN + "web/index.html?order_id=";

    public static void setEnvironment(DkEnvironment environment) {
        DkConstant.environment = environment;
        DK_DOMIN = environment == DkEnvironmentDev ? DK_DOMAIN_DEV
                : (environment == DkEnvironmentStage ? DK_DOMAIN_STAGE : DK_DOMAIN_PRODUCT);
        DK_PHANTOMJS_WEB_URL = DK_DOMIN + "web/index.html?order_id=";
    }
}
