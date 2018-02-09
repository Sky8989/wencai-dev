package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.GiftCard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
    int updateGiftCard(GiftCard giftCard);

    /**
     * CMS 系统 删除礼品卡信息
     */
    int deleteGiftCard(int id);

}
