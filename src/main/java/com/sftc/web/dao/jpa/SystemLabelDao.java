package com.sftc.web.dao.jpa;


import com.sftc.web.model.entity.SystemLabel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SystemLabelDao extends PagingAndSortingRepository<SystemLabel, Integer>, JpaSpecificationExecutor<SystemLabel> {

}
