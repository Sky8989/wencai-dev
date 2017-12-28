package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.entity.CommonQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "常见问题列表响应")
public class CommonQuestionListVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "CommonQuestion",value = "常见问题列表")
    private List<CommonQuestion> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
