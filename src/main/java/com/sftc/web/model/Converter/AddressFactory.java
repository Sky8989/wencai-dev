package com.sftc.web.model.Converter;

import com.sftc.web.model.dto.AddressDTO;
import com.sftc.web.model.entity.Address;

/**
 * Created by xf on 2017/10/1.
 */
public class AddressFactory {
    public static AddressDTO entityToDto(Address address){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setUser_id(address.getUser_id());
        addressDTO.setName(address.getName());
        addressDTO.setPhone(address.getPhone());
        addressDTO.setProvince(address.getProvince());
        addressDTO.setCity(address.getCity());
        addressDTO.setArea(address.getArea());
        addressDTO.setAddress(address.getAddress());
        addressDTO.setSupplementary_info(address.getSupplementary_info());
        addressDTO.setLongitude(address.getLongitude());
        addressDTO.setLatitude(address.getLatitude());
        addressDTO.setCreate_time(address.getCreate_time());
        return  addressDTO;
    }
}
