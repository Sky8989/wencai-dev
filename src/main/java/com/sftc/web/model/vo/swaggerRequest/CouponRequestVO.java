package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by xf on 2017/10/18.
 * @author Administrator
 */
@ApiModel(value = "优惠券查询包装类")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CouponRequestVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "优惠券状态",example = "all",required = true)
    private String status;

	@Override
	public String toString() {
		return "CouponRequestVO [status=" + status + "]";
	}
}
