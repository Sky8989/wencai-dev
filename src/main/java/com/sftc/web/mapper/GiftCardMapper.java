package com.sftc.web.mapper;

import com.sftc.web.model.GiftCard;
import com.sftc.web.model.Order;

import java.util.List;

public interface GiftCardMapper {
    Order giftCardDetail(String order_number);
    List<GiftCard> giftCardList();

    /** 根据id查询贺卡 */
    GiftCard selectGiftCardById(int giftCardId);

    //     <!--下面是cms系统用到的mapper-->
    List<GiftCard> selectByPage(GiftCard giftCard);
}
