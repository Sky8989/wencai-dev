package com.sftc.web.mapper;

import com.sftc.web.model.Address;
import com.sftc.web.model.AddressBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AddressBookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AddressBook record);

    int insertSelective(AddressBook record);

    AddressBook selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AddressBook record);

    int updateByPrimaryKey(AddressBook record);

}
