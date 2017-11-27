package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AddressMapper {

    /**
     * 添加地址
     * @param address
     */
    void addAddress(Address address);

    /**
     * 删除地址
     * @param id
     */
    void deleteAddress(int id);
}
