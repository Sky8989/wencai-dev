package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "短信验证设备信息包装类")
public class SMSMessageDetailVO {
    @ApiModelProperty(name = "model",value = "手机型号",example = "iPhone 6 Plus",required = true)
    private String model;
    @ApiModelProperty(name = "pixelRatio",value = "电话号码",example = "3",required = true)
    private String pixelRatio;
    @ApiModelProperty(name = "screenWidth",value = "屏宽",example = "414",required = true)
    private String screenWidth;
    @ApiModelProperty(name = "screenHeight",value = "屏高",example = "736",required = true)
    private String screenHeight;
    @ApiModelProperty(name = "windowWidth",value = "窗宽",example = "414",required = true)
    private String windowWidth;
    @ApiModelProperty(name = "windowHeight",value = "窗高",example = "672",required = true)
    private String windowHeight;
    @ApiModelProperty(name = "system",value = "系统版本",example = "iOS 10.0.1",required = true)
    private String system;
    @ApiModelProperty(name = "language",value = "语言",example = "zh_CN",required = true)
    private String language;
    @ApiModelProperty(name = "version",value = "版本",example = "6.5.6",required = true)
    private String version;
    @ApiModelProperty(name = "brand",value = "商标",example = "devtools",required = true)
    private String brand;
    @ApiModelProperty(name = "platform",value = "平台",example = "devtools",required = true)
    private String platform;
    @ApiModelProperty(name = "SDKVersion",value = "SDK版本",example = "1.6.0",required = true)
    private String SDKVersion;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPixelRatio() {
        return pixelRatio;
    }

    public void setPixelRatio(String pixelRatio) {
        this.pixelRatio = pixelRatio;
    }

    public String getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(String screenWidth) {
        this.screenWidth = screenWidth;
    }

    public String getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(String screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(String windowWidth) {
        this.windowWidth = windowWidth;
    }

    public String getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(String windowHeight) {
        this.windowHeight = windowHeight;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSDKVersion() {
        return SDKVersion;
    }

    public void setSDKVersion(String SDKVersion) {
        this.SDKVersion = SDKVersion;
    }
}
