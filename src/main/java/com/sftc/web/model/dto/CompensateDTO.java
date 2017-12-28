package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Compensate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/10/18.
 * @author Administrator
 */
@ApiModel(value = "赔偿规则包装类")
public class CompensateDTO{

    @Getter @Setter
    @ApiModelProperty(name = "compensates",value = "赔偿规则说明")
    private List<Compensate> compensates;
    
}
