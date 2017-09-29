package com.sftc.web.dao.mybatis;

import com.sftc.web.model.AddressHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressHistoryMapper {

    /**
     * 插入历史地址
     */
    void insertAddressHistory(AddressHistory addressHistory);

    /**
     * 查询历史地址
     */
    List<AddressHistory> selectAddressHistoryListByUserId(
            @Param("user_id") int user_id,
            @Param("startIndex") int startIndex,
            @Param("pageSize") int pageSize
    );

    /**
     * 删除历史地址（软删除）
     */
    void deleteAddressHistoryWithId(int address_history_Id);
}
