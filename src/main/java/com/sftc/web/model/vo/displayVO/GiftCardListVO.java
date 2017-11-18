package com.sftc.web.model.vo.displayVO;

import com.sftc.web.model.entity.GiftCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 礼品卡界面展示类
 */
@ApiModel(value = "礼品卡列表")
public class GiftCardListVO {

    @Setter @Getter
    @ApiModelProperty(name = "type",value = "礼品卡类型",example = "节日必备")
    private String type;

    @Setter @Getter
    @ApiModelProperty(name = "giftCards",value = "礼品卡列表")
    private List<GiftCard> giftCards;
}
