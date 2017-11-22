package com.sftc.web.dao.jpa;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sftc.web.model.entity.SystemLabel;

public interface SystemLabelDao extends PagingAndSortingRepository<SystemLabel, Integer>, JpaSpecificationExecutor<SystemLabel> {

}
