package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderExpressTransform;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExpressTransformMapper {

    @Update("UPDATE c_order_no_driver SET is_read = 1 WHERE id = #{id}")
    void updateExpressTransformReadStatusById(@Param("id") int id);

    @Select("SELECT * from c_order_no_driver WHERE same_uuid = #{same_uuid} AND is_read = 0")
    OrderExpressTransform selectExpressTransformByUUID(@Param("same_uuid") String uuid);

    @Select("SELECT * from c_order_no_driver WHERE id = #{id}")
    OrderExpressTransform selectExpressTransformByID(@Param("id") int id);
}
