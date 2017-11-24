package com.sftc.web.model.vo.swaggerRequestVO.priceExaplain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "删除价格说明")
public class DeletePriceExplain {
	
	 @ApiModelProperty(name="价格说明id",required=true)
	    @Setter @Getter
	    private int id;

}
