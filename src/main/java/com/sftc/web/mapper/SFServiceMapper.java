package com.sftc.web.mapper;

import com.sftc.web.model.sfmodel.SFServiceAddress;
import org.apache.ibatis.annotations.Insert;

public interface SFServiceMapper {

    @Insert("INSERT INTO sftc_service_address (distId,level,code,parentId,parentCode,countryCode,name,lang,available,opening)" +
            "VALUES (#{distId},#{level},#{code},#{parentId},#{parentCode},#{countryCode},#{name},#{lang},#{available},#{opening})")
    void insertSFServiceAddress(SFServiceAddress serviceAddress);
}
