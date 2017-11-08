package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "大网订单提交请求包装类")
public class OrderNationRequestVO {
    @ApiModelProperty(name = "sf",value = "下大网单发送给顺丰的信息")
    private OrderNationSfVO sf;
    @ApiModelProperty(name = "order",value = "大网单的订单信息")
    private OrderNationMessage order;
    @ApiModelProperty(name = "packages",value = "大网单的包裹信息")
    private List<Packages> packages;

    public OrderNationSfVO getSf() {
        return sf;
    }

    public void setSf(OrderNationSfVO sf) {
        this.sf = sf;
    }

    public OrderNationMessage getOrder() {
        return order;
    }

    public void setOrder(OrderNationMessage order) {
        this.order = order;
    }

    public List<Packages> getPackages() {
        return packages;
    }

    public void setPackages(List<Packages> packages) {
        this.packages = packages;
    }
}
