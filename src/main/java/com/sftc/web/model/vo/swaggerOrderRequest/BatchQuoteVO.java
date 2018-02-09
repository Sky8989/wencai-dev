package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 多包裹每个包裹对应的Quote的uuid值
 *
 * @author ： CatalpaFlat
 * @date ：Create in 13:43 2017/11/20
 */
@ApiModel(value = "好友多包裹每个包裹对应的Quote的uuid值")
public class BatchQuoteVO {
    @Getter
    @Setter
    @ApiModelProperty(name = "uuid",example = "2c9a85895d97c789015d982f0b28023a",required = true)
    @NotBlank(message = "uuid不能为空")
    private String uuid;
}
