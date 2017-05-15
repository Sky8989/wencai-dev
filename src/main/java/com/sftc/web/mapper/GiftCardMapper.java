package com.sftc.web.mapper;

import com.sftc.web.model.GiftCard;
import com.sftc.web.model.Order;

import java.util.List;

public interface GiftCardMapper {
    Order giftCardDetail(String order_number);
    List<GiftCard> giftCardList(String type);
}
