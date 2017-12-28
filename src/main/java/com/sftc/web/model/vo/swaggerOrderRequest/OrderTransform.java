package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "同城转大网的快递uuid")
public class OrderTransform extends BaseVO {
    @ApiModelProperty(name = "uuid",example = "2c9a8589600bed7501600c3329580664",required = true)
    @NotBlank(message = "uuid参数不能为空")
    private String uuid;

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}
}
