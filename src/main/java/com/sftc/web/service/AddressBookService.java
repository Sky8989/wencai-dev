package com.sftc.web.service;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;


public interface AddressBookService {

    /**
     * 新增地址条目
     */
    APIResponse addAddressBook(APIRequest apiRequest);

    /**
     * 删除地址条目
     *
     * @param apiRequest
     * @return
     */
    APIResponse deleteAddressBook(APIRequest apiRequest);

    /**
     * 修改地址条目
     *
     * @param apiRequest
     * @return
     */
    APIResponse updateAddressBook(APIRequest apiRequest);

    /**
     * 获取 地址簿列表 根据时间戳降序排列 大的时间戳在上面
     *
     * @param apiRequest
     * @return
     */
    APIResponse selectAddressBookList(APIRequest apiRequest);

}
