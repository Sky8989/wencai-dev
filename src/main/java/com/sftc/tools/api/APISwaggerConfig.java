package com.sftc.tools.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.sftc.web.controller.app")
public class APISwaggerConfig {
    @Bean
    public Docket createAPI() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("Token安全验证")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("a9081b1158e650b3985424")
                .required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2).forCodeGeneration(true).select().apis(RequestHandlerSelectors.any())
                //过滤生成链接
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        Contact contact = new Contact("Dankal", "http://xyj.dankal.cn/dashboard/#!/project/1iDRGSOLWL", "bingo@dankal.cn");

        return new ApiInfoBuilder().license("Apache License Version 2.0").title("顺丰同城专送C端").description("小程序服务端接口文档").contact(contact).version("1.0").build();
    }

}