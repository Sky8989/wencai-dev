package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单信息")
public class FriendOrderMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "order_id",example = "C150841092812641")
    @NotBlank(message = "order_id参数不能为空")
    private String order_id;

    @Getter @Setter
    @ApiModelProperty(name = "reserve_time",value = "预约时间",example = "1501497840000")
    private String reserve_time;

    @Getter @Setter
    @ApiModelProperty(name = "form_id",value = "表单Id",example = "1501497840000")
    private String form_id;

}
