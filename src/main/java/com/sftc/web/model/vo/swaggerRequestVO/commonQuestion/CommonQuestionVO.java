package com.sftc.web.model.vo.swaggerRequestVO.commonQuestion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "问题说明查询 list并分页 新增id为0，修改时传id")
public class CommonQuestionVO{

    @ApiModelProperty(name="主键")
    @Setter @Getter
    private int id;
    @ApiModelProperty(name="当前页",example="1",required = true)
    @Setter @Getter
    private int pageNumKey;
    @ApiModelProperty(name="每页显示个数",example="5",required = true)
    @Setter @Getter
    private int pageSizeKey;
    
    @ApiModelProperty(name="标题",example = "问题标题，模糊查询")
    @Setter @Getter
    private String title;

    @ApiModelProperty(name="内容",example = "问题答案，模糊查询")
    @Setter @Getter
    private String content;


}
