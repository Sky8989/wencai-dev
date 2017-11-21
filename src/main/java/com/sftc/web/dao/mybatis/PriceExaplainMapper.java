package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.PriceExplain;
import org.apache.ibatis.annotations.Param;


public interface PriceExaplainMapper {
    PriceExplain queryPriceExplainByCirty(@Param(value = "city") String city);

	int updatePriceExplain(PriceExplain priceExplain);

	int deletePriceExplain(int id);
}
