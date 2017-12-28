package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Address;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper {

    void addAddress(Address address);

    void editeAddress(Address address);

    List<Address> addressDetail(String userUUId);

    void deleteAddress(int id);
}
