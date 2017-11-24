package com.sftc.web.model.vo.swaggerRequestVO.userContactLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@ApiModel(value = "删除用户标签")
public class DeleteUserContactLabelVo{

    @ApiModelProperty(name="用户标签id",required=true)
    @Setter @Getter
    private int id;
    
}
