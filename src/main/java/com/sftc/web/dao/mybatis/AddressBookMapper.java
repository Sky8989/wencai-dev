package com.sftc.web.dao.mybatis;

import com.sftc.web.model.AddressBook;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressBookMapper {

    void insert(AddressBook record);

    void insertSelective(AddressBook record);

    AddressBook selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(AddressBook record);

    void updateByCreatetime(@Param("id") int id, @Param("create_time") String create_time);

    void updateIsDeleteStatusByPrimaryKey(@Param("id") int id, @Param("is_delete") int is_delete);

    List<AddressBook> selectAddressBookList(@Param("user_id") int user_id, @Param("address_book_type") String address_book_type);

    List<AddressBook> selectAddressHistoryListByUserId(@Param("user_id") int user_id, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    // 查找重复的记录 可设置address_type address_book_type来区分各种地址映射
    List<AddressBook> selectAddressForRemoveDuplicate(
            @Param("user_id") int user_id,
            @Param("address_type") String address_type,
            @Param("address_book_type") String address_book_type,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("province") String province,
            @Param("city") String city,
            @Param("area") String area,
            @Param("address") String address,
            @Param("supplementary_info") String supplementary_info
    );

    // 查找重复的记录 可设置address_type address_book_type来区分各种地址映射
    List<AddressBook> selectDuplicateAddress(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("province") String province,
            @Param("city") String city,
            @Param("area") String area,
            @Param("address") String address,
            @Param("supplementary_info") String supplementary_info
    );

}
