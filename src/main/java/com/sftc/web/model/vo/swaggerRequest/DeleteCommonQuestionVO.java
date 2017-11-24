package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "删除问题说明 ")
public class DeleteCommonQuestionVO {
	
	@ApiModelProperty(name="系统标签id",required=true)
    @Setter @Getter
    private int id;

}
