package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单填写包装类")
public class FriendFillVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "ship_name",value = "收件人姓名",example = "庄槟豪测试订单",required = true)
    private String ship_name;

    @Getter @Setter
    @ApiModelProperty(name = "ship_mobile",value = "收件人电话",example = "13544185508",required = true)
    private String ship_mobile;

    @Getter @Setter
    @ApiModelProperty(name = "ship_province",value = "收件人省份",example = "广东省",required = true)
    private String ship_province;

    @Getter @Setter
    @ApiModelProperty(name = "ship_city",value = "收件人城市",example = "深圳市",required = true)
    private String ship_city;

    @Getter @Setter
    @ApiModelProperty(name = "ship_area",value = "收件人区域",example = "龙岗区",required = true)
    private String ship_area;

    @Getter @Setter
    @ApiModelProperty(name = "ship_addr",value = "收件人详细地址",example = "龙翔大道2188号",required = true)
    private String ship_addr;

    @Getter @Setter
    @ApiModelProperty(name = "supplementary_info",value = "收件人门牌号",example = "好友填写3号")
    private String supplementary_info;

    @Getter @Setter
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508415669414OY",required = true)
    private String order_id;

    @Getter @Setter
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.9466987556842",dataType = "double",required = true)
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.530164470515828",dataType = "double",required = true)
    private double latitude;
}
