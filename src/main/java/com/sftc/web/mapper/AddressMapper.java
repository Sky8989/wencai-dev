package com.sftc.web.mapper;

import com.sftc.web.model.Address;

import java.util.List;
import java.util.Map;

public interface AddressMapper {

    void addAddress(Address address);

    void editeAddress(Address address);

    List<Address> addressDetail(int id);

    List<Address> addressList(Map<String,Integer> params);

    void deleteAddress(int id);
}
