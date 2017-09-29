package com.sftc.web.mapper;

import com.sftc.web.model.GiftCard;

import java.util.List;

public interface GiftCardMapper {

    List<GiftCard> giftCardList();

    /**
     * 根据id查询贺卡
     */
    GiftCard selectGiftCardById(int giftCardId);

    /**
     * CMS 查找礼品卡信息  分页加条件加模糊
     */
    List<GiftCard> selectByPage(GiftCard giftCard);

    /**
     * CMS 系统 添加礼品卡信息
     */
    void insertGiftCard(GiftCard giftCard);

    /**
     * CMS 系统 修改礼品卡信息
     */
    void updateGiftCard(GiftCard giftCard);

    /**
     * CMS 系统 删除礼品卡信息
     */
    void deleteGiftCard(int id);

}
