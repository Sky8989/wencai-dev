package com.sftc.web.model.vo.swaggerRequest.commonQuestion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "查询问题list并分页 新增不用传id，修改时传id")
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

    @ApiModelProperty(name="标题",example = "问题标题，模糊查询",required=false)
    @Setter @Getter
    private String title;
    
    @ApiModelProperty(name="内容",example = "问题答案，模糊查询",required=false)
    @Setter @Getter
    private String content;
   
}
