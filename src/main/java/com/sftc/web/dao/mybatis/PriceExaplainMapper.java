package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.PriceExplain;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PriceExaplainMapper {
    PriceExplain queryPriceExplainByCirty(@Param(value = "city") String city);

	int updatePriceExplain(PriceExplain priceExplain);

	int deletePriceExplain(int id);

    List<String> queryCityName();
}
