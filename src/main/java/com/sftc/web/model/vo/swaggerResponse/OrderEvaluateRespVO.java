package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.others.EvaluateAttributes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "评价订单响应对象")
public class OrderEvaluateRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "EvaluateAttributes",value = "订单评价列表")
    private EvaluateAttributes result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
