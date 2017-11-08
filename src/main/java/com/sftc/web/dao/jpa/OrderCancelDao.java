package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderCancel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderCancelDao extends PagingAndSortingRepository<OrderCancel, Integer>, JpaSpecificationExecutor<OrderCancel> {

}
