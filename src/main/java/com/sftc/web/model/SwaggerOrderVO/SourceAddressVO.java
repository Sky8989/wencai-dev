package com.sftc.web.model.SwaggerOrderVO;

import com.sftc.web.model.SwaggerRequestVO.CoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "寄件人地址信息")
public class SourceAddressVO {

    @ApiModelProperty(name = "address",value = "寄件人地址信息")
    private OrderAddressVO address;
    @ApiModelProperty(name = "coordinate",value = "经纬度")
    private CoordinateVO coordinate;

    public OrderAddressVO getAddress() {
        return address;
    }

    public void setAddress(OrderAddressVO address) {
        this.address = address;
    }

    public CoordinateVO getCoordinate() {return coordinate;}

    public void setCoordinate(CoordinateVO coordinate) {this.coordinate = coordinate;}
}
