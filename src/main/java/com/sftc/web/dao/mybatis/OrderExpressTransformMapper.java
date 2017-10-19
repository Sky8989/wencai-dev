package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.OrderExpressTransform;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExpressTransformMapper {

    String sftc_order_express_transform = "sftc_order_express_transform";

//    @Insert("INSERT INTO " + sftc_order_express_transform + "(express_id, same_uuid, nation_uuid, is_read, create_time) " +
//            "VALUES (#{express_id}, #{same_uuid}, #{nation_uuid}, #{is_read}, #{create_time})")
//    void insertExpressTransform(OrderExpressTransform orderExpressTransform);

//    @Update("UPDATE sftc_order_express_transform SET is_read = 1 WHERE id = #{id}")
//    void updateExpressTransformReadStatusById(@Param("id") int id);


    @Select("SELECT * from sftc_order_express_transform WHERE nation_uuid = #{nation_uuid}")
    OrderExpressTransform selectExpressTransformByUUID(@Param("nation_uuid") String uuid);

    @Select("SELECT * from sftc_order_express_transform WHERE id = #{id}")
    OrderExpressTransform selectExpressTransformByID(@Param("id") int id);
}