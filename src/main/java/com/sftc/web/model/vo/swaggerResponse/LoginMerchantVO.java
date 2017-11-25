package com.sftc.web.model.vo.swaggerResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "登录后返回信息")
public class LoginMerchantVO{

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "uuid",example = "2c9a85895cf1d58d015cf81be418170c")
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "name",value = "姓名",example = "Merchant_1d79")
    private String name;

    @Getter @Setter
    @ApiModelProperty(name = "account_type",value = "类型",example = "MOBILE")
    private String account_type;

    @Getter @Setter
    @ApiModelProperty(name = "mobile",value = "手机号",example = "13797393543")
    private String mobile;

    @Getter @Setter
    @ApiModelProperty(name = "avatar",value = "头像链接",example = "http://od4jhik93.bkt.clouddn.com/default_avatar.jpg")
    private String avatar;

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "状态",example = "SIGNED_CONTRACT")
    private String status;

    @Getter @Setter
    @ApiModelProperty(name = "summary")
    private LoginSummaryVO summary;

    @Getter @Setter
    @ApiModelProperty(name = "attributes")
    private LoginAttributesVO attributes;

    @Getter @Setter
    @ApiModelProperty(name = "tags")
    private List<LoginTagesVO> tags;

    @Getter @Setter
    @ApiModelProperty(name = "created_at",value = "时间",example = "2017-06-30T16:26:36.000+0800")
    private String created_at;
}
