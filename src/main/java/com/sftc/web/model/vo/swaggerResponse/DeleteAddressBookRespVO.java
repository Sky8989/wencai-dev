package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.entity.AddressBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "删除地址簿响应对象")
public class DeleteAddressBookRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "addressBookDTOList",value = "历史地址列表")
    private AddressBook result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
