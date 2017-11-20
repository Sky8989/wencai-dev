package com.sftc.web.model.dto;
import com.sftc.web.model.vo.displayVO.PackageMessageVO;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.OrderExpress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel(value = "快递扩展信息包装类")
public class OrderExpressDTO extends OrderExpress {

    @Getter
    @Setter
    @ApiModelProperty(name = "gift_card_id",value = "礼卡表id",example = "17",dataType = "int")
    private int gift_card_id;    // 礼卡表id

    @Getter
    @Setter
    @ApiModelProperty(name = "ship_avatar",value = "收件人头像")
    private String ship_avatar;  // extension 收件人头像

    @Getter
    @Setter
    @ApiModelProperty(name = "packageMessage",value = "包裹信息")
    private PackageMessageVO packageMessage;

    public OrderExpressDTO() {
    }

    @Getter
    @Setter
    @ApiModelProperty(name = "evaluate",value = "评价信息",hidden = true)
    private Evaluate evaluate;  // 评价信息

    @Getter
    @Setter
    @ApiModelProperty(name = "orderExpressList",value = "快递数组",hidden = true)
    private List<OrderExpress> orderExpressList;   //快递数组
}