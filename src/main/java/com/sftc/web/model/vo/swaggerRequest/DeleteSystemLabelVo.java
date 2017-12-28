package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "删除系统标签")
public class DeleteSystemLabelVo{

    @ApiModelProperty(name="系统标签id")
    @Setter @Getter
    private int id;
    
}
