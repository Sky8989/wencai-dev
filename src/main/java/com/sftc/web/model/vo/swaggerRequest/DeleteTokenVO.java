package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "删除token的请求包装类")
public class DeleteTokenVO extends BaseVO {

    @ApiModelProperty(name = "mobile",value = "电话",example = "17679122584")
    private String mobile;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}
}
