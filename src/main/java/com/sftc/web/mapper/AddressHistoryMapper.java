package com.sftc.web.mapper;

import com.sftc.web.model.AddressHistory;

import java.util.List;
import java.util.Map;

public interface AddressHistoryMapper {
    AddressHistory addressHistoryDetail(int id);
    List<AddressHistory> addressHistoryList(Map<String,Integer> params);
}
