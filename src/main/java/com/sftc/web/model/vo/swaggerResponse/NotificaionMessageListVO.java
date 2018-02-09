package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.dto.MessageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "消息提醒通知响应对象")
public class NotificaionMessageListVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "MessageDTO",value = "消息提醒通知列表")
    private List<MessageDTO> result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;

}
