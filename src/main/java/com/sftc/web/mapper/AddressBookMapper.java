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

    void updateByCreatetime(@Param("id") int id, @Param("create_time") String create_time);

    int updateIsDeleteStatusByPrimaryKey(@Param("id") int id, @Param("is_delete") int is_delete);

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
            @Param("supplementary_info") String supplementary_info,
            @Param("longitude") String longitude,
            @Param("latitude") String latitude
    );

}
