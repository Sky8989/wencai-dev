package com.sftc.web.dao.mybatis;

import com.sftc.web.model.sfmodel.SFServiceAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface SFServiceAddressMapper {

    @Insert("INSERT INTO sftc_service_address (distId,level,code,parentId,parentCode,countryCode,name,lang,available,opening)" +
            "VALUES (#{distId},#{level},#{code},#{parentId},#{parentCode},#{countryCode},#{name},#{lang},#{available},#{opening})")
    void insertSFServiceAddress(SFServiceAddress serviceAddress);

    @Select("SELECT * FROM sftc_service_address WHERE name=#{name}")
    SFServiceAddress selectServiceAddressByName(
            @Param("name") String name
    );

    @Select("SELECT * FROM sftc_service_address WHERE name=#{name} AND level=#{level}")
    SFServiceAddress selectServiceAddressByNameAndLevel(
            @Param("name") String name,
            @Param("level") int level
    );
}
