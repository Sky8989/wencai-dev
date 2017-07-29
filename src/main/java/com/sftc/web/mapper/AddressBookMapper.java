package com.sftc.web.mapper;

import com.sftc.web.model.Address;
import com.sftc.web.model.AddressBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AddressBookMapper {

    /**
     * 添加 地址簿条目信息
     *
     * @param addressBook
     */
    void addAddressBook(AddressBook addressBook);

    /**
     * 删除 地址簿条目信息
     *
     * @param user_id
     * @param addressBook_id
     */
    void deleteAddressBook(@Param("user_id") int user_id, @Param("id") int addressBook_id);

    /**
     * 查询 地址簿条目信息列表
     *
     * @param user_id
     * @return
     */
    List<AddressBook> selectList(int user_id);

    /**
     * 修改 地址簿条目信息
     *
     * @param addressBook
     */
    void updateAddressBook(AddressBook addressBook);

}
