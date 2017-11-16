package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "收件人经纬度")
public class TargetLLtudeVO {
    @ApiModelProperty(name = "longitude",value = "经度",example = "114.26088879416709")
    private String longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.72294328112597")
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
