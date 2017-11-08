package com.sftc.web.dao.jpa;

import com.sftc.web.model.CommonQuestion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommonQuestionDao extends PagingAndSortingRepository<CommonQuestion, Long>, JpaSpecificationExecutor<CommonQuestion> {

}
