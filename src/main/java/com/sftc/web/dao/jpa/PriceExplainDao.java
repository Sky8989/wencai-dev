package com.sftc.web.dao.jpa;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sftc.web.model.entity.PriceExplain;

public interface PriceExplainDao extends PagingAndSortingRepository<PriceExplain, Integer>, JpaSpecificationExecutor<PriceExplain> {

}
