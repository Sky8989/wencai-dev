package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderExpressTransform;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExpressTransformMapper {

    @Update("UPDATE sftc_order_no_driver SET is_read = 1 WHERE id = #{id}")
    void updateExpressTransformReadStatusById(@Param("id") int id);

    @Select("SELECT * from sftc_order_no_driver WHERE id = #{id}")
    OrderExpressTransform selectExpressTransformByID(@Param("id") int id);
}
