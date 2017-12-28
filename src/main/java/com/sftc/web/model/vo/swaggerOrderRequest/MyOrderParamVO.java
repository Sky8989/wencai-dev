package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "我的订单列表请求包装类")
public class MyOrderParamVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "userUuid",value = "用户uuid",example = "10093",hidden = true)
    private String userUuid;

    @Getter @Setter
    @ApiModelProperty(name = "token",value = "用户access_token",example = "EyMivbd44I124lcddrBG",hidden = true)
    private String token;

    @Getter @Setter
    @ApiModelProperty(name = "pageNum",value = "第几页",example = "1",required = true)
    private int pageNum;

    @Getter @Setter
    @ApiModelProperty(name = "pageSize",value = "每页几条",example = "10",required = true)
    private int pageSize;

    @Getter @Setter
    @ApiModelProperty(name = "keyword",value = "关键字")
    private String keyword;

    @Getter @Setter
    @ApiModelProperty(name = "keyword_state",value = "关键字状态")
    private String keyword_state;
}
