package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AddressMapper {

    void addAddress(Address address);

    void editeAddress(Address address);

    List<Address> addressDetail(int id);

    List<Address> addressList(Map<String, Integer> params);

    void deleteAddress(int id);

    void updateByPrimaryKey(Address address);

    void deleteAddressById(@Param("id") int id);

    /**
     * 根据手机号码和经纬度查询地址
     */
    List<Address> selectAddressByPhoneAndLongitudeAndLatitude(@Param("phone") String phone, @Param("longitude") double longitude, @Param("latitude") double latitude);
}
