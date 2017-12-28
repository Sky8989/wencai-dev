package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.ApiResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "添加地址簿响应对象")
public class AddAddressBookRespVO extends ApiResponse {

    @Getter @Setter
    @ApiModelProperty(name = "AddressBookResp",value = "地址簿信息")
    private AddressBookResp result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
