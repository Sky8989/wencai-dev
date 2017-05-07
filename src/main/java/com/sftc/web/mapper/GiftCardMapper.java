package com.sftc.web.mapper;

import com.sftc.web.model.Order;

public interface GiftCardMapper {
    Order giftCardDetail(String order_number);
}
