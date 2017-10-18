package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressBookDao extends PagingAndSortingRepository<AddressBook, Long>, JpaSpecificationExecutor<AddressBook> {

}
