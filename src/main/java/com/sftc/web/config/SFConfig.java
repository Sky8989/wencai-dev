package com.sftc.web.config;

import lombok.Getter;
import lombok.Setter;

public class SFConfig {

    @Getter
    @Setter
    public static String SF_DOMAIN_DEV = ""; // 顺丰开发环境域名

    @Getter
    @Setter
    public static String SF_DOMAIN_STAGE = ""; // 顺丰测试环境域名

    @Getter
    @Setter
    public static String SF_DOMAIN_PRODUCT = ""; // 顺丰生产环境域名

    @Getter
    @Setter
    public static String SF_COMMON_TOKEN_USERANEM = ""; // 生产环境的获取公共token的账号用户名

    @Getter
    @Setter
    public static String SF_COMMON_TOKEN_PASSWORD = ""; // 生产环境的获取公共token的账号密码
}
