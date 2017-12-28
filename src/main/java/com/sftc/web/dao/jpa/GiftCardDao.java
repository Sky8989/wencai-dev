package com.sftc.web.dao.jpa;


import com.sftc.web.model.entity.GiftCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GiftCardDao extends PagingAndSortingRepository<GiftCard, Integer>, JpaSpecificationExecutor<GiftCard> {

}
