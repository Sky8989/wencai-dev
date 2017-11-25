package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.web.model.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "地址信息对象")
public class AddressMessageVO extends Address {

    @ApiModelProperty(name = "id",value = "主键",example = "2304",dataType = "int")
    @Setter @Getter
    private int id;

    @ApiModelProperty(name = "user_id",value = "用户id",example = "10084",dataType = "int")
    @Setter @Getter
    private int user_id;
}
