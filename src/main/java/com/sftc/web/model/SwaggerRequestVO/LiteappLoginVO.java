package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "小程序用户刷新token请求包装类")
public class LiteappLoginVO {

    @ApiModelProperty(name = "jscode",value = "jscode",example = "jscode",required = true)
    private String jscode;
    @ApiModelProperty(name = "iv",value = "加密数据",example = "加密数据")
    private String iv;
    @ApiModelProperty(name = "encryptedData",value = "初始向量",example = "初始向量")
    private String encryptedData;

    public String getJscode() {return jscode;}

    public void setJscode(String jscode) {this.jscode = jscode;}

    public String getIv() {return iv;}

    public void setIv(String iv) {this.iv = iv;}

    public String getEncryptedData() {return encryptedData;}

    public void setEncryptedData(String encryptedData) {this.encryptedData = encryptedData;}
}
