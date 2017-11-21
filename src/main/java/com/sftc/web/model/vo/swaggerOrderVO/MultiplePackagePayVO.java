package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 17:39 2017/11/21
 */
@ApiModel(description = "多包裹支付")
public class MultiplePackagePayVO {
    @Getter
    @Setter
    @NotBlank(message = "group_uuid不能为空")
    @ApiModelProperty(name = "group_uuid",value = "uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    private String group_uuid;
}
