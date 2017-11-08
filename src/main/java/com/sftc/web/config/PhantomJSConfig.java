package com.sftc.web.config;

/**
 * PhantomJS配置
 * Created by bingo on 30/09/2017.
 */
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

    public void setSHELL_PATH(String shellPath) {
        SHELL_PATH = shellPath;
    }

    public void setJS_PATH(String jsPath) {
        JS_PATH = jsPath;
    }

    public void setOUTPUT_PATH(String outputPath) {
        OUTPUT_PATH = outputPath;
    }
}
