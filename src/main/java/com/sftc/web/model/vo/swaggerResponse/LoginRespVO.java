package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.vo.swaggerRequestVO.CoordinateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "用户登录响应对象")
public class LoginRespVO extends APIResponse{

    @Getter @Setter
    @ApiModelProperty(name = "merchant")
    private LoginMerchantVO result;

    @Getter @Setter
    @ApiModelProperty(name = "error",value = "错误信息",hidden = true)
    private Object error;
}
