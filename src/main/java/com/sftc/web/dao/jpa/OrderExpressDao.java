package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.OrderExpress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderExpressDao extends PagingAndSortingRepository<OrderExpress, Integer>, JpaSpecificationExecutor<OrderExpress> {

}
