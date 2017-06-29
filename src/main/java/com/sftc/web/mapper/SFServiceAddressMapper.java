package com.sftc.web.mapper;

import com.sftc.web.model.sfmodel.SFServiceAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface SFServiceAddressMapper {

    @Insert("INSERT INTO sftc_service_address (distId,level,code,parentId,parentCode,countryCode,name,lang,available,opening)" +
            "VALUES (#{distId},#{level},#{code},#{parentId},#{parentCode},#{countryCode},#{name},#{lang},#{available},#{opening})")
    void insertSFServiceAddress(SFServiceAddress serviceAddress);

    @Select("SELECT * FROM sftc_service_address WHERE name=#{name} AND level=#{level}")
    SFServiceAddress selectServiceAddressByNameAndLevel(
            @Param("name") String name,
            @Param("level") int level
    );

    @Select("SELECT * FROM sftc_service_address WHERE name=#{name} AND level=#{level} AND parentCode=#{parentCode}")
    SFServiceAddress selectServiceAddressByNameAndLevelAndParentCode(
            @Param("name") String name,
            @Param("level") int level,
            @Param("parentCode") String parentCode
    );
}
