package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单时间规则包装类")
public class OrderContantsVO {
    @ApiModelProperty(name = "access_token",value = "顺丰access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.947074",required = true)
    private String longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.530375",required = true)
    private String latitude;
    @ApiModelProperty(name = "constants",value = "常量",example = "BASICDATA",required = true)
    private String constants;

    public String getAccess_token() {return access_token;}

    public void setAccess_token(String access_token) {this.access_token = access_token;}

    public String getLongitude() {return longitude;}

    public void setLongitude(String longitude) {this.longitude = longitude;}

    public String getLatitude() {return latitude;}

    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getConstants() {return constants;}

    public void setConstants(String constants) {this.constants = constants;}
}
