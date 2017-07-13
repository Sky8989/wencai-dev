package com.sftc.web.mapper;

import com.sftc.web.model.GiftCard;
import com.sftc.web.model.Order;

import java.util.List;

public interface GiftCardMapper {
    Order giftCardDetail(String order_number);

    List<GiftCard> giftCardList();

    /**
     * 根据id查询贺卡
     */
    GiftCard selectGiftCardById(int giftCardId);

    /**
     * CMS 查找礼品卡信息  分页加条件加模糊
     *
     * @param giftCard
     * @return
     */
    List<GiftCard> selectByPage(GiftCard giftCard);

    /**
     * CMS 系统 添加礼品卡信息
     *
     * @param giftCard
     */
    void insertGiftCard(GiftCard giftCard);

    /**
     * CMS 系统 修改礼品卡信息
     *
     * @param giftCard
     */
    void updateGiftCard(GiftCard giftCard);

    /**
     * CMS 系统 删除礼品卡信息
     *
     * @param id
     */
    void deleteGiftCard(int id);

}
