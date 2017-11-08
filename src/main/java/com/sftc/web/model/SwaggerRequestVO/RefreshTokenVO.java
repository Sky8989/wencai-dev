package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "刷新token请求包装类")
public class RefreshTokenVO {

    @ApiModelProperty(name = "mobileLoginVO")
    private MobileLoginVO mobileLoginVO;
    @ApiModelProperty(name = "wxLoginVO"    )
    private WXLoginVO wxLoginVO;
    @ApiModelProperty(name = "liteappLogin")
    private LiteappLoginVO liteappLogin;
    @ApiModelProperty(name = "system_refresh_token",value = "刷新token",example = "716ff6b16857ba09118a4b",required = true)
    private String system_refresh_token;

    public MobileLoginVO getMobileLoginVO() {return mobileLoginVO;}

    public void setMobileLoginVO(MobileLoginVO mobileLoginVO) {this.mobileLoginVO = mobileLoginVO;}

    public WXLoginVO getWxLoginVO() {return wxLoginVO;}

    public void setWxLoginVO(WXLoginVO wxLoginVO) {this.wxLoginVO = wxLoginVO;}

    public LiteappLoginVO getLiteappLogin() {return liteappLogin;}

    public void setLiteappLogin(LiteappLoginVO liteappLogin) {this.liteappLogin = liteappLogin;}

    public String getSystem_refresh_token() {return system_refresh_token;}

    public void setSystem_refresh_token(String system_refresh_token) {this.system_refresh_token = system_refresh_token;}
}
