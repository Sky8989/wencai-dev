package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.GiftCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCardMapper {

    /**
     * 获取礼品卡列表
     * @return
     */
    List<GiftCard> giftCardList();

    GiftCard selectGiftCardById(@Param("id") int id);
}
