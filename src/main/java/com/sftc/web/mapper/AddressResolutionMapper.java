package com.sftc.web.mapper;

import com.sftc.web.model.AddressResolution;
import org.apache.ibatis.annotations.Param;

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
