package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.swaggerRequest.TargetCoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "收件人地址信息")
public class TargetAddressVO {

    @ApiModelProperty(name = "address",value = "寄件人地址信息")
    private OrderTargetAddressVO address;
    @ApiModelProperty(name = "coordinate",value = "经纬度")
    private TargetCoordinateVO coordinate;

    public OrderTargetAddressVO getAddress() {return address;}

    public void setAddress(OrderTargetAddressVO address) {this.address = address;}

    public TargetCoordinateVO getCoordinate() {return coordinate;}

    public void setCoordinate(TargetCoordinateVO coordinate) {this.coordinate = coordinate;}
}
