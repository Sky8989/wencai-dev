package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.AddressBookDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "历史地址列表响应")
public class AddressHistoryListVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "addressBookDTOList",value = "历史地址列表")
    private List<AddressBookDTO> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
