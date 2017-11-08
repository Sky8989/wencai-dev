package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDao extends PagingAndSortingRepository<Order, String>, JpaSpecificationExecutor<Order> {

}
