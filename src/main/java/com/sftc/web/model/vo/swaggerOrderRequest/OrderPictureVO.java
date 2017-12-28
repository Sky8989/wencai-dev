package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单分享截图请求包装类")
public class OrderPictureVO extends BaseVO {
    @ApiModelProperty(name = "order_id", example = "C1508233636870TR", required = true)
    @NotBlank(message = "order_id参数不能为空")
    private String order_id;
    @ApiModelProperty(name = "name", example = "悟空", required = true)
    private String name;

    public String getOrder_id() {return order_id;}

    public void setOrder_id(String order_id) {this.order_id = order_id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
