package com.sftc.tools.utils;

import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.entity.AddressBook;

import java.util.List;

/**
 * 下单和填写订单后通用地址簿插入 utils
 *
 * @author ： xfan
 * @date ：Create in 11:51 2017/11/29
 */
public class InsertAddressBookUtil {

    public void insertAddressBookUtils(
            String addressType, String addressBookType, String senderUserUuid, String shipUserUuid, String name, String phone,
            String province, String city, String area, String address, String supplementaryInfo, String createTime, double longitude,
            double latitude, AddressBookMapper addressBookMapper, AddressMapper addressMapper, AddressBookDao addressBookDao) {

        com.sftc.web.model.entity.Address addressParam = new com.sftc.web.model.entity.Address(
                shipUserUuid, name, phone,
                province, city, area, address, supplementaryInfo,
                longitude, latitude, createTime
        );

        // 查找重复信息
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressForRemoveDuplicate(senderUserUuid,
                addressType, addressBookType, name, phone,
                province, city, area, address, supplementaryInfo);
        // 0代表无重复信息
        if (addressBookDTOList.size() == 0) {
            //执行插入操作
            addressMapper.addAddress(addressParam);
            AddressBook addressBook = new AddressBook(senderUserUuid, addressParam.getId(), 0, 0, addressType, addressBookType, createTime);
            addressBookDao.save(addressBook);
        }
    }
}
