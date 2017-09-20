package com.sftc.web.service.impl;


import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressBookMapper;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.model.Address;
import com.sftc.web.model.AddressBook;
import com.sftc.web.service.AddressBookService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
@Service("addressBookService")
public class AddressBookServiceImpl implements AddressBookService {
    @Resource
    private AddressBookMapper addressBookMapper;

    @Resource
    private AddressMapper addressMapper;


    public APIResponse addAddressBook(APIRequest apiRequest) {
        ///验参
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramObject.containsKey("user_id")) return APIUtil.paramErrorResponse("地址簿参数user_id为空");
        if (!paramObject.containsKey("is_delete")) return APIUtil.paramErrorResponse("地址簿参数is_delete为空");
        if (!paramObject.containsKey("is_mystery")) return APIUtil.paramErrorResponse("地址簿参数is_mystery为空");
        if (!paramObject.containsKey("address_type")) return APIUtil.paramErrorResponse("地址簿参数address_type为空");
        if (!paramObject.containsKey("address_book_type")) return APIUtil.paramErrorResponse("地址簿参数address_book_type为空");
        if (!paramObject.containsKey("address")) return APIUtil.paramErrorResponse("地址簿参数address为空");
        JSONObject address_OBJ = paramObject.getJSONObject("address");
        if (address_OBJ.containsValue("")) return APIUtil.paramErrorResponse("地址簿参数不可为''");
        if (!address_OBJ.containsKey("name")) return APIUtil.paramErrorResponse("地址簿参数name为空");
        if (!address_OBJ.containsKey("phone")) return APIUtil.paramErrorResponse("地址簿参数phone为空");
        if (!address_OBJ.containsKey("province")) return APIUtil.paramErrorResponse("地址簿参数province为空");
        if (!address_OBJ.containsKey("city")) return APIUtil.paramErrorResponse("地址簿参数city为空");
        if (!address_OBJ.containsKey("area")) return APIUtil.paramErrorResponse("地址簿参数area为空");
        if (!address_OBJ.containsKey("address")) return APIUtil.paramErrorResponse("地址簿参数address为空");
        if (!address_OBJ.containsKey("longitude")) return APIUtil.paramErrorResponse("地址簿参数longitude为空");
        if (!address_OBJ.containsKey("latitude")) return APIUtil.paramErrorResponse("地址簿参数latitude为空");

        String supplementary_info = address_OBJ.containsKey("supplementary_info") ? address_OBJ.getString("supplementary_info") : null;
        AddressBook addressBook;
        Address address;
        try {
            addressBook = (AddressBook) JSONObject.toBean(paramObject, AddressBook.class);
            address = (Address) JSONObject.toBean(address_OBJ, Address.class);
        } catch (JSONException e) {
            return APIUtil.paramErrorResponse("参数含有非法字符");
        }

        // 查找重复信息  去重
        List<AddressBook> addressBookList = addressBookMapper.selectAddressForRemoveDuplicate(
                paramObject.getInt("user_id"),
                paramObject.getString("address_type"),
                paramObject.getString("address_book_type"),
                address_OBJ.getString("name"),
                address_OBJ.getString("phone"),
                address_OBJ.getString("province"),
                address_OBJ.getString("city"),
                address_OBJ.getString("area"),
                address_OBJ.getString("address"),
                supplementary_info
        );

        if (addressBookList.size() != 0) return APIUtil.submitErrorResponse("地址簿已有该地址", null);

        String create_time = Long.toString(System.currentTimeMillis());
        // 插入地址记录
        address.setCreate_time(create_time);
        address.setUser_id(addressBook.getUser_id());
        addressMapper.addAddress(address);
        // 插入地址簿记录
        addressBook.setCreate_time(create_time);
        addressBook.setAddress_id(address.getId());
        addressBookMapper.insertSelective(addressBook);

        addressBook.setAddress(address);
        return APIUtil.getResponse(SUCCESS, addressBook);
    }


    public APIResponse deleteAddressBook(APIRequest apiRequest) {
        ///验参
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        if (httpServletRequest.getParameter("addressBook_id") == null || "".equals(httpServletRequest.getParameter("addressBook_id")))
            return APIUtil.paramErrorResponse("addressBook_id参数为空");
        int addressBook_id = Integer.parseInt(httpServletRequest.getParameter("addressBook_id"));

        AddressBook addressBook = new AddressBook();
        addressBook.setId(addressBook_id);
        addressBook.setIs_delete(1);
        /// 执行删除操作
        addressBookMapper.updateIsDeleteStatusByPrimaryKey(addressBook_id, 1);
        return APIUtil.getResponse(SUCCESS, addressBook);
    }


    public APIResponse updateAddressBook(APIRequest apiRequest) {
        // 修改地址时 改变创建时间 以供查询地址簿列表时根据时间排序
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        AddressBook addressBookParam = (AddressBook) JSONObject.toBean(paramObject, AddressBook.class);

        ///更新 地址簿记录时间 包括映射关系和地址实体的时间
        String create_time = Long.toString(System.currentTimeMillis());

        //TODO 修改地址映射的时间
        addressBookParam.setCreate_time(create_time);

        // 查找重复信息  去重
        JSONObject address_OBJ = paramObject.getJSONObject("address");
        String supplementary_info = address_OBJ.containsKey("supplementary_info") ? address_OBJ.getString("supplementary_info") : null;
        List<AddressBook> addressBookList = addressBookMapper.selectDuplicateAddress(
                address_OBJ.getString("name"),
                address_OBJ.getString("phone"),
                address_OBJ.getString("province"),
                address_OBJ.getString("city"),
                address_OBJ.getString("area"),
                address_OBJ.getString("address"),
                supplementary_info
        );

        if (addressBookList.size() != 0){
            addressBookMapper.updateByCreatetime(addressBookList.get(0).getId(), create_time);
        } else {
            addressBookMapper.updateByPrimaryKeySelective(addressBookParam);
            AddressBook addressBook = addressBookMapper.selectByPrimaryKey(addressBookParam.getId());

            //TODO 修改地址实体的时间
            Address address = addressBookParam.getAddress();
            address.setId(addressBook.getAddress_id());
            address.setCreate_time(create_time);
            addressMapper.updateByPrimaryKey(address);
        }

        return APIUtil.getResponse(SUCCESS, null);
    }


    public APIResponse selectAddressBookList(APIRequest apiRequest) {
        /// 处理参数
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        int user_id = Integer.parseInt(httpServletRequest.getParameter("user_id"));
        String address_book_type = httpServletRequest.getParameter("address_book_type");

        //执行查询
        List<AddressBook> addressBooks = addressBookMapper.selectAddressBookList(user_id, address_book_type);
        if (addressBooks.size() > 0) {
            return APIUtil.getResponse(SUCCESS, addressBooks);
        } else {
            return APIUtil.selectErrorResponse("用户无地址簿信息", null);
        }
    }


}
