package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexMapper {

    /**
     * 常见问题列表
     */
    List<CommonQuestion> getCommonQuestion();

    List<Compensate> getCompensate();

    ServicePhone getServicePhoneByCity(@Param(value = "city") String city);

    PriceExplain queryPriceExplainByCirty(@Param(value = "city") String city);

    List<String> queryCityName();

    /**
     * 获取礼品卡列表
     * @return
     */
    List<GiftCard> giftCardList();

    GiftCard selectGiftCardById(@Param("id") int id);

    /**
     * 查询同城支持城市列表
     *
     * @return
     */
    List<CityExpress> quaryCityExpressList();

}
