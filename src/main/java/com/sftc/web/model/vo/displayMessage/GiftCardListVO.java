package com.sftc.web.model.vo.displayMessage;

import com.sftc.web.model.entity.GiftCard;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 礼品卡界面展示类
 */
public class GiftCardListVO {

    @Setter @Getter
    private String type;

    @Setter @Getter
    private List<GiftCard> giftCards;
}
