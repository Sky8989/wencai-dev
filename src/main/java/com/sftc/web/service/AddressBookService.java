package com.sftc.web.service;


import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.AddressBookDeleteVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookRequestVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookUpdateVO;

/**
 * 
 * @author Administrator
 *
 */
public interface AddressBookService {

    /**
     * 新增地址条目
     */
    ApiResponse addAddressBook(AddressBookRequestVO body);

    /**
     * 删除地址条目
     *
     * @param body
     * @return
     */
    ApiResponse deleteAddressBook(AddressBookDeleteVO body);

    /**
     * 修改地址条目
     *
     * @param body
     * @return
     */
    ApiResponse updateAddressBook(AddressBookUpdateVO body);

    /**
     * 获取 地址簿列表 根据时间戳降序排列 大的时间戳在上面
     *
     * @param addressBookType
     * @return
     */
    ApiResponse selectAddressBookList(String addressBookType);
    /**
     * 查询历史地址
     * @param pageNum 当前页
     * @param pageSize 每页显示个数
     * @return
     */
    ApiResponse selectAddressHistory(Integer pageNum, Integer pageSize);
}
