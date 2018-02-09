package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.AddressResolution;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressResolutionMapper {

    /**
     * 简单查找地址解析记录
     */
    AddressResolution selectAddressResolution(@Param("address") String address);

    /**
     * 插入地址解析记录
     */
    void insertAddressResolution(AddressResolution addressResolution);

}
