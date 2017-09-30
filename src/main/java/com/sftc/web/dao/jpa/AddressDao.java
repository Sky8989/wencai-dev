package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressDao extends PagingAndSortingRepository<Address, Long>, JpaSpecificationExecutor<Address> {

}
