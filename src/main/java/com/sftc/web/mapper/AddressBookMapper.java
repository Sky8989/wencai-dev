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

    int updateIsDeleteStatusByPrimaryKey(@Param("id") int id, @Param("is_delete") int is_delete);

    List<AddressBook> selectAddressBookList(@Param("user_id") int user_id, @Param("address_book_type") String address_book_type);

    List<AddressBook> selectAddressHistoryListByUserId(int user_id, int startIndex, int pageSize);

}
