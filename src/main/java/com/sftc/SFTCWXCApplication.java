package com.sftc;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * start
 *
 * @author ： CatalpaFlat
 */
@Controller
@EnableScheduling
@ServletComponentScan
@SpringBootApplication
@MapperScan(basePackages = "com.sftc.web.dao.mybatis")
@PropertySource("classpath:properties/system.properties")
public class SFTCWXCApplication {
    private static final Logger logger = LoggerFactory.getLogger(SFTCWXCApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SFTCWXCApplication.class, args);
        logger.info("SFTCWXCApplication start success...");
    }
}


