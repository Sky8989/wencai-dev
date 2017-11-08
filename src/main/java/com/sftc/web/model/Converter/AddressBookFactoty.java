package com.sftc.web.model.Converter;

import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.entity.AddressBook;

/**
 * Created by xf on 2017/9/30.
 */
public class AddressBookFactoty {
    public static AddressBook dtoToEntity(AddressBookDTO addressBookDTO){
        AddressBook addressBook = new AddressBook();
        addressBook.setAddress_book_type(addressBookDTO.getAddress_book_type());
        addressBook.setAddress_type(addressBookDTO.getAddress_type());
        addressBook.setIs_delete(addressBookDTO.getIs_delete());
        addressBook.setIs_mystery(addressBookDTO.getIs_mystery());
        addressBook.setUser_id(addressBookDTO.getUser_id());
        addressBook.setCreate_time(addressBookDTO.getCreate_time());
        addressBook.setId(addressBookDTO.getId());
        addressBook.setAddress_id(addressBookDTO.getAddress_id());
        return  addressBook;
    }
}