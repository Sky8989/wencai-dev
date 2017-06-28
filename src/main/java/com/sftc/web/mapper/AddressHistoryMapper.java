package com.sftc.web.mapper;

import com.sftc.web.model.AddressHistory;

import java.util.List;

public interface AddressHistoryMapper {

    /**
     * 插入历史地址
     */
    void insertAddressHistory(AddressHistory addressHistory);

    /**
     * 查询历史地址
     */
    List<AddressHistory> selectAddressHistoryListByUserId(int user_id);
}
