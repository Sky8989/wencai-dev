package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "删除礼品卡 ")
public class DeleteGiftCardVO {
	
	@ApiModelProperty(name="礼品卡id",required=true)
    @Setter @Getter
    private int id;

}
