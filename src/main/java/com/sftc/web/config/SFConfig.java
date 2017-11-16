package com.sftc.web.config;

/**
 * 顺丰接口相关配置
 * Created by bingo on 30/09/2017.
 */
public class SFConfig {

    /**
     * 顺丰同城 开发环境域名
     */
    public static String SF_SAME_DOMAIN_DEV = "";

    /**
     * 顺丰同城 测试环境域名
     */
    public static String SF_SAME_DOMAIN_STAGE = "";

    /**
     * 顺丰同城 生产环境域名
     */
    public static String SF_SAME_DOMAIN_PRODUCT = "";

    /**
     * 顺丰同城 生产环境的获取公共token的账号用户名
     */
    public static String SF_SAME_COMMON_TOKEN_USERNAME = "";

    /**
     * 顺丰同城 生产环境的获取公共token的账号密码
     */
    public static String SF_SAME_COMMON_TOKEN_PASSWORD = "";

    public void setSF_SAME_DOMAIN_DEV(String sF_SAME_DOMAIN_DEV) {
        SF_SAME_DOMAIN_DEV = sF_SAME_DOMAIN_DEV;
    }

    public void setSF_SAME_DOMAIN_STAGE(String sF_SAME_DOMAIN_STAGE) {
        SF_SAME_DOMAIN_STAGE = sF_SAME_DOMAIN_STAGE;
    }

    public void setSF_SAME_DOMAIN_PRODUCT(String sF_SAME_DOMAIN_PRODUCT) {
        SF_SAME_DOMAIN_PRODUCT = sF_SAME_DOMAIN_PRODUCT;
    }

    public void setSF_SAME_COMMON_TOKEN_USERNAME(String sF_SAME_COMMON_TOKEN_USERNAME) {
        SF_SAME_COMMON_TOKEN_USERNAME = sF_SAME_COMMON_TOKEN_USERNAME;
    }

    public void setSF_SAME_COMMON_TOKEN_PASSWORD(String sF_SAME_COMMON_TOKEN_PASSWORD) {
        SF_SAME_COMMON_TOKEN_PASSWORD = sF_SAME_COMMON_TOKEN_PASSWORD;
    }
}
