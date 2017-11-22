package com.sftc.web.dao.jpa;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sftc.web.model.entity.UserContactLabel;

public interface UserContactLabelDao extends PagingAndSortingRepository<UserContactLabel, Integer>, JpaSpecificationExecutor<UserContactLabel> {

}
