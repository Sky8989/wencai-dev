package com.sftc.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 顺丰接口相关配置
 * Created by bingo on 30/09/2017.
 */
@Component
public class SfConfig {

    /**
     * 顺丰同城 开发环境域名
     */
    public static String SF_SAME_DOMAIN_DEV;

    /**
     * 顺丰同城 测试环境域名
     */
    public static String SF_SAME_DOMAIN_STAGE;

    /**
     * 顺丰同城 生产环境域名
     */
    public static String SF_SAME_DOMAIN_PRODUCT;

    /**
     * 顺丰大网 开发环境域名
     */
    public static String SF_NATION_DOMAIN_DEV;

    /**
     * 顺丰大网 生产环境域名
     */
    public static String SF_NATION_DOMAIN_PRODUCT;

    /**
     * 顺丰同城 生产环境的获取公共token的账号用户名
     */
    public static String SF_SAME_COMMON_TOKEN_USERNAME;

    /**
     * 顺丰同城 生产环境的获取公共token的账号密码
     */
    public static String SF_SAME_COMMON_TOKEN_PASSWORD;


    @Value("${sf-api.domain.same.dev}")
    public  void setSF_SAME_DOMAIN_DEV(String sfSameDomainDev) {
        SF_SAME_DOMAIN_DEV = sfSameDomainDev;
    }

    @Value("${sf-api.domain.same.stage}")
    public  void setSF_SAME_DOMAIN_STAGE(String sfSameDomainStage) {
        SF_SAME_DOMAIN_STAGE = sfSameDomainStage;
    }

    @Value("${sf-api.domain.same.product}")
    public  void setSF_SAME_DOMAIN_PRODUCT(String sfSameDomainProduct) {
        SF_SAME_DOMAIN_PRODUCT = sfSameDomainProduct;
    }

    @Value("${sf-api.domain.nation.dev}")
    public  void setSF_NATION_DOMAIN_DEV(String sfNationDomainDev) {
        SF_NATION_DOMAIN_DEV = sfNationDomainDev;
    }

    @Value("${sf-api.domain.nation.product}")
    public  void setSF_NATION_DOMAIN_PRODUCT(String sfNationDomainProduct) {
        SF_NATION_DOMAIN_PRODUCT = sfNationDomainProduct;
    }

    @Value("${sf-api.same.common.username}")
    public  void setSfSameCommonTokenUsername(String sfSameCommonTokenUsername) {
        SF_SAME_COMMON_TOKEN_USERNAME = sfSameCommonTokenUsername;
    }

    @Value("${sf-api.same.common.password}")
    public  void setSfSameCommonTokenPassword(String sfSameCommonTokenPassword) {
        SF_SAME_COMMON_TOKEN_PASSWORD = sfSameCommonTokenPassword;
    }
}
