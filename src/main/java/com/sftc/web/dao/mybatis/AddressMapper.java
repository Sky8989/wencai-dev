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

    void deleteAddress(int id);
}
