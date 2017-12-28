package com.sftc.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * PhantomJS配置
 * Created by bingo on 30/09/2017.
 */
@Component
public class PhantomJSConfig {

    /**
     * phantomJS 脚本路径
     */
    public static String SHELL_PATH = "";

    /**
     * phantomJS js路径
     */
    public static String JS_PATH = "";

    /**
     * phantomJS 截图保存路径
     */
    public static String OUTPUT_PATH = "";

    @Value("${phantomJS.path.js}")
    public  void setJS_PATH(String js_path) {
        JS_PATH = js_path;
    }

    @Value("${phantomJS.path.output}")
    public  void setOUTPUT_PATH(String output_path) {
        OUTPUT_PATH = output_path;
    }

    @Value("${phantomJS.path.shell}")
    public  void setSHELL_PATH(String shell_path) {
        SHELL_PATH = shell_path;
    }
}
