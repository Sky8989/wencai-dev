package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "手机用户刷新token请求包装类")
public class MobileLoginVO {

    @ApiModelProperty(name = "mobile",value = "电话",example = "17679122584",required = true)
    private String mobile;
    @ApiModelProperty(name = "content",value = "验证码",example = "1234",required = true)
    private String content;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}
}
