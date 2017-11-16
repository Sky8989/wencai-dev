package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价请求信息")
public class EvaluateMessageVO {
    @ApiModelProperty(name = "access_token",value = "顺丰access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;
    @ApiModelProperty(name = "attributes",value = "评价内容")
    private EvaluateAttributesVO attributes;
    @ApiModelProperty(name = "user_id",value = "用户id",example = "10093",required = true)
    private String user_id;
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    private String uuid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public EvaluateAttributesVO getAttributes() {
        return attributes;
    }

    public void setAttributes(EvaluateAttributesVO attributes) {
        this.attributes = attributes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}