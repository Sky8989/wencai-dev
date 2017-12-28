package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "设置兜底信息已读的请求包装类")
public class OrderTransformIsReadVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "express_transform_id",value = "快递兜底id",example = "225",dataType = "int")
    @NotBlank(message = "transform_id参数不能为空")
    private Integer express_transform_id;

}
