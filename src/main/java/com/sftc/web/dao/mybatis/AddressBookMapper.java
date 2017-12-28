package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.AddressBookDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressBookMapper {

    AddressBookDTO selectByPrimaryKey(Long id);

    void deleteAddressBookById(@Param("id") long id);

    List<AddressBookDTO> selectAddressBookList(@Param("userUUID") String userUUID, @Param("address_book_type") String address_book_type);

    List<AddressBookDTO> selectAddressHistoryListByUserUUId(@Param("userUUID") String userUUID, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

   // 查找重复的记录 可设置address_type address_book_type来区分各种地址映射
    List<AddressBookDTO> selectAddressForRemoveDuplicate(
            @Param("userUUID") String userUUID,
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
   List<AddressBookDTO> selectDuplicateAddress(
           @Param("name") String name,
           @Param("phone") String phone,
           @Param("province") String province,
           @Param("city") String city,
           @Param("area") String area,
           @Param("address") String address,
           @Param("supplementary_info") String supplementary_info
   );

}
