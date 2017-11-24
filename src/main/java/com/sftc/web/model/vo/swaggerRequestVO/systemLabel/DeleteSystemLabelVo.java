package com.sftc.web.model.vo.swaggerRequestVO.systemLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@ApiModel(value = "删除系统标签")
public class DeleteSystemLabelVo{

    @ApiModelProperty(name="系统标签id")
    @Setter @Getter
    private int id;
    
}
