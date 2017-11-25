package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.AddressBookDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "请求响应信息对象")
public class ResponseMessageVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "result",value = "响应结果",hidden = true)
    private Object result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
