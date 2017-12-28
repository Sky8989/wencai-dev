package com.sftc.web.dao.jpa;


import com.sftc.web.model.entity.UserContactLabel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserContactLabelDao extends PagingAndSortingRepository<UserContactLabel, Integer>, JpaSpecificationExecutor<UserContactLabel> {

}
