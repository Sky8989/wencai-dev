package com.sftc.tools.constant;

import static com.sftc.tools.constant.ThirdPartyConstant.QN_DOMAIN;

/**
 * 蛋壳系统常量
 */
public class DKConstant {

    /**
     * 蛋壳域名
     */
    private static final String DK_DOMIN = "https://sftc.dankal.cn/";

    /**
     * 默认头像占位图
     */
    public static final String DK_USER_AVATAR_DEFAULT = QN_DOMAIN + "anim_package.png";

    /**
     * 默认分页参数 每页的数量
     */
    public static final int DK_PAGE_SIZE_DEFAULT = 20;

    /**
     * phantomJS 配置
     */
    private static final String DK_PHANTOMJS_PATH = "/data/wwwroot/sftc.dankal.cn/phantomjs/";
//    private static final String DK_PHANTOMJS_PATH =  "/Users/bingo/Desktop/phantomjs-2.1.1-macosx/"; // bingo.local

    /**
     * phantomJS 脚本路径
     */
    public static final String DK_PHANTOMJS_SHELLPATH = DK_PHANTOMJS_PATH + "bin/phantomjs";
    /**
     * phantomJS js路径
     */
    public static final String DK_PHANTOMJS_JSPATH = DK_PHANTOMJS_PATH + "request.js";
    /**
     * phantomJS 截图保存路径
     */
    public static final String DK_PHANTOMJS_OUTPUTPATH = DK_PHANTOMJS_PATH + "images/";
    /**
     * phantomJS 页面地址
     */
    public static final String DK_PHANTOMJS_WEB_URL = DK_DOMIN + "web/index.html?order_id=";
    /**
     * phantomJS 图片地址
     */
    public static final String DK_PHANTOMJS_IMAGES = DK_DOMIN + "phantomjs/images/";
}
