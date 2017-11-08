package com.sftc.web.dao.jpa;

import com.sftc.web.model.entity.OrderExpressTransform;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderExpressTransformDao extends PagingAndSortingRepository<OrderExpressTransform, Integer>, JpaSpecificationExecutor<OrderExpressTransform> {

}
