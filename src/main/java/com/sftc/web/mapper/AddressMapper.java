package com.sftc.web.mapper;

import com.sftc.web.model.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AddressMapper {

    void addAddress(Address address);

    void editeAddress(Address address);

    List<Address> addressDetail(int id);

    List<Address> addressList(Map<String, Integer> params);

    void deleteAddress(int id);

    int updateByPrimaryKey(Address address);

    /**
     * 根据手机号码和经纬度查询地址
     */
    List<Address> selectAddressByPhoneAndLongitudeAndLatitude(@Param("phone") String phone, @Param("longitude") double longitude, @Param("latitude") double latitude);
}
