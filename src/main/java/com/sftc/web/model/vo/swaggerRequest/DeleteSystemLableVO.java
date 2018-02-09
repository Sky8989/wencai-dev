package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "用户标签")
public class DeleteSystemLableVO {

    @Getter @Setter
    @ApiModelProperty(value = "标签id", example = "1", required = true,dataType = "int")
    private int id;
}
