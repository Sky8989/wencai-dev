package com.sftc.web.model.vo.swaggerOrderVO;

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
    private OrderNationMessageVO order;
    @ApiModelProperty(name = "packages",value = "大网单的包裹信息")
    private List<PackagesVO> packages;

    public OrderNationSfVO getSf() {
        return sf;
    }

    public void setSf(OrderNationSfVO sf) {
        this.sf = sf;
    }

    public OrderNationMessageVO getOrder() {
        return order;
    }

    public void setOrder(OrderNationMessageVO order) {
        this.order = order;
    }

    public List<PackagesVO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackagesVO> packages) {
        this.packages = packages;
    }
}
