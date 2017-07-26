package com.sftc.web.mapper;

import com.sftc.web.model.Address;
import com.sftc.web.model.AddressBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AddressBookMapper {

    void addAddressBook(AddressBook addressBook);

    void deleteAddressBook(@Param("user_id") int user_id, @Param("id") int addressBook_id);

    List<AddressBook> selectList(int user_id);

}
