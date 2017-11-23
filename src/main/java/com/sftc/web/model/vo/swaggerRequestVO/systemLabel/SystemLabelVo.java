package com.sftc.web.model.vo.swaggerRequestVO.systemLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@ApiModel(value = "查询问题list并分页")
public class SystemLabelVo{

    @ApiModelProperty(name="主键",required=false)
    @Setter @Getter
    private int id;
    @ApiModelProperty(name="当前页",example="1")
    @Setter @Getter
    private int pageNumKey;
    @ApiModelProperty(name="每页显示个数",example="5")
    @Setter @Getter
    private int pageSizeKey;

    @ApiModelProperty(name="系统标签内容",example = "系统标签内容，模糊查询",required=false)
    @Setter @Getter
    private String system_label;
    
}