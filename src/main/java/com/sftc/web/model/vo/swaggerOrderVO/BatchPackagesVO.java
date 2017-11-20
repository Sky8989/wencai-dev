package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 批量计价/订单包裹对象
 *
 * @author ： CatalpaFlat
 * @date ：Create in 17:47 2017/11/17
 */
@ApiModel(description = "批量计价/订单包裹对象")
public class BatchPackagesVO {
    @Getter
    @Setter
    @ApiModelProperty(name = "type", value = "包裹类型", example = "FILE")
    @NotBlank(message = "type不能为空")
    private String type;
    @Getter
    @Setter
    @ApiModelProperty(name = "weight", value = "包裹重量", example = "3")
    @NotBlank(message = "weight不能为空")
    private Integer weight;
    @Getter
    @Setter
    @ApiModelProperty(name = "item_amount", value = "数量", example = "1")
    @NotBlank(message = "item_amount不能为空")
    private Integer item_amount;
    @Getter
    @Setter
    @ApiModelProperty(name = "comments", value = "包裹描述", example = "大家电测试包裹描述")
    @NotBlank(message = "comments不能为空")
    private String comments;

}
