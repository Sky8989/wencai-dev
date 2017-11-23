package com.sftc.web.dao.jpa;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sftc.web.model.entity.GiftCard;

public interface GiftCardDao extends PagingAndSortingRepository<GiftCard, Integer>, JpaSpecificationExecutor<GiftCard> {

}
