package com.sftc.web.mapper;

import com.sftc.web.model.AddressResolution;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huxingyue on 2017/7/19.
 */
public interface AddressResolutionMapper {
    /**
     * 简单查找地址解析记录
     *
     * @param address
     * @return
     */
    AddressResolution selectAddressResolution(@Param("address") String address);

    /**
     * 插入地址解析记录
     *
     * @param addressResolution
     */
    void insertAddressResolution(AddressResolution addressResolution);

}
