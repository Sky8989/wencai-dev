package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/18.
 * @author Administrator
 */
@ApiModel(value = "经纬度包装类")
public class CoordinateVO extends BaseVO {

    @ApiModelProperty(name = "longitude",value = "经度",example = "113.9387000000",dataType = "double",required = true)
    private Double longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.5248300000",dataType = "double",required = true)
    private Double latitude;

    public Double getLongitude() {return longitude;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public Double getLatitude() {return latitude;}

    public void setLatitude(Double latitude) {this.latitude = latitude;}

	@Override
	public String toString() {
		return "CoordinateVO [longitude=" + longitude + ", latitude=" + latitude + "]";
	}
    
}
