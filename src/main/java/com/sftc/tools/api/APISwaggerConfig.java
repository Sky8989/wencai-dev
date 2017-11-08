package com.sftc.tools.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.sftc.web.controller.app")
public class APISwaggerConfig {
    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2).forCodeGeneration(true).select().apis(RequestHandlerSelectors.any())
                //过滤生成链接
                .paths(PathSelectors.any()).build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        Contact contact = new Contact("bingo", "http://xyj.dankal.cn/dashboard/#!/project/1iDRGSOLWL", "bingo@dankal.cn");
        ApiInfo apiInfo = new ApiInfoBuilder().license("Apache License Version 2.0").title("顺丰同城专送").description("DANKAL-JavaEE-API-Docs").contact(contact).version("1.0").build();

        return apiInfo;
    }

}