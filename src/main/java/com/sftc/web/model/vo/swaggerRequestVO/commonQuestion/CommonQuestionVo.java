package com.sftc.web.model.vo.swaggerRequestVO.commonQuestion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@ApiModel(value = "系统标签查询 list并分页 新增不用传id，修改时传id")
public class CommonQuestionVo{

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
