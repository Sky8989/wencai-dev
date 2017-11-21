package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "寄件人经纬度")
public class SourceLLtudeVO {
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.94524651122737",required = true)
    private String longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.5304852403182",required = true)
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
