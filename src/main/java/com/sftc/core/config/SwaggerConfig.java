package com.sftc.core.config;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置
 *
 * @author 陈梓平
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.sftc.web.controller")
public class SwaggerConfig {
    @Bean
    public Docket createAPI() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("Token安全验证")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("359a0c6e37b8a540ea7c9d")
                .required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.sftc.web.controller"))
                //过滤生成链接
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        springfox.documentation.service.Contact contact = new springfox.documentation.service.Contact("Dankal", "http://xyj.dankal.cn/dashboard/#!/project/1iDRGSOLWL", "bingo@dankal.cn");

        return new ApiInfoBuilder().license("Apache License Version 2.0").title("顺丰同城专送C端").description("小程序服务端接口文档").contact(contact).version("1.0").build();
    }


}
