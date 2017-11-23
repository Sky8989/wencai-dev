package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 */
@ApiModel(value = "收件人经纬度包装类")
public class TargetCoordinateVO {

    @ApiModelProperty(name = "longitude",value = "经度",example = "114.25885",dataType = "double")
    private double longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.72007",dataType = "double")
    private double latitude;

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}
}
