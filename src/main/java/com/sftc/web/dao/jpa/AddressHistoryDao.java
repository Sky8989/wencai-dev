package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.model.entity.AddressHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressHistoryDao extends PagingAndSortingRepository<AddressHistory, Integer>, JpaSpecificationExecutor<AddressHistory> {

}
