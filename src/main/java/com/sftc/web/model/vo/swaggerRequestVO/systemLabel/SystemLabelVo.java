package com.sftc.web.model.vo.swaggerRequestVO.systemLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel(value = "系统标签list并分页")
public class SystemLabelVo{

    @ApiModelProperty(name="主键",required=true)
    @Setter @Getter
    private int id;
    @ApiModelProperty(name="当前页",example="1",required=true)
    @Setter @Getter
    private int pageNumKey;
    @ApiModelProperty(name="每页显示个数",example="5",required=true)
    @Setter @Getter
    private int pageSizeKey;

    @ApiModelProperty(name="系统标签内容",example = "系统标签内容，模糊查询",required=true)
    @Setter @Getter
    private String system_label;
    
}
