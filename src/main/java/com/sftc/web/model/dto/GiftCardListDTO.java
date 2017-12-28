package com.sftc.web.model.dto;

import com.sftc.web.model.entity.GiftCard;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 礼品卡
 */
public class GiftCardListDTO {

    @Setter @Getter
    private String type;

    @Setter @Getter
    private List<GiftCard> giftCards;
}
