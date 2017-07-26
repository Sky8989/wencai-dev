package com.sftc.web.service.impl;


import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressBookMapper;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.mapper.AddressResolutionMapper;
import com.sftc.web.model.Address;
import com.sftc.web.model.AddressBook;
import com.sftc.web.model.AddressResolution;
import com.sftc.web.service.AddressBookService;
import com.sftc.web.service.AddressService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_ADDRESS_DISTANCE_URL;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_GEOCODER_URL;

@Service("addressBookService")
public class AddressBookServiceImpl implements AddressBookService {
    @Resource
    private AddressBookMapper addressBookMapper;


    public APIResponse addAddressBook(APIRequest apiRequest) {
        ///验参
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramObject.containsKey("user_id")) {
            return APIUtil.paramErrorResponse("参数有误user_id");
        } else if (!paramObject.containsKey("name")) {
            return APIUtil.paramErrorResponse("参数有误name");
        } else if (!paramObject.containsKey("phone")) {
            return APIUtil.paramErrorResponse("参数有误phone");
        } else if (!paramObject.containsKey("province")) {
            return APIUtil.paramErrorResponse("参数有误province");
        } else if (!paramObject.containsKey("city")) {
            return APIUtil.paramErrorResponse("参数有误city");
        } else if (!paramObject.containsKey("area")) {
            return APIUtil.paramErrorResponse("参数有误area");
        }
        AddressBook addressBook = (AddressBook) JSONObject.toBean(paramObject, AddressBook.class);
        addressBook.setCreate_time(Long.toString(System.currentTimeMillis()));
        addressBookMapper.addAddressBook(addressBook);

        return APIUtil.getResponse(SUCCESS, addressBook);
    }


    public APIResponse deleteAddressBook(APIRequest apiRequest) {
        ///验参
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramObject.containsKey("user_id")) {
            return APIUtil.paramErrorResponse("参数有误,user_id");
        } else if (!paramObject.containsKey("addressBook_id")) {
            return APIUtil.paramErrorResponse("参数有误,addressBook_id");
        }
        int user_id;
        int addressBook_id;
        try {
            user_id = paramObject.getInt("user_id");
            addressBook_id = paramObject.getInt("addressBook_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return APIUtil.paramErrorResponse("参数有误，非自然数");
        }
        /// 执行删除操作
        addressBookMapper.deleteAddressBook(user_id, addressBook_id);
        return APIUtil.getResponse(SUCCESS, null);
    }


    public APIResponse updateAddressBook(APIRequest apiRequest) {
        // 修改地址时 改变创建时间 以供查询地址簿列表时根据时间排序
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        AddressBook addressBook = (AddressBook) JSONObject.toBean(paramObject, AddressBook.class);

        addressBook.setCreate_time(Long.toString(System.currentTimeMillis()));
        addressBookMapper.updateAddressBook(addressBook);

        return APIUtil.getResponse(SUCCESS,addressBook);
    }


    public APIResponse selectAddressBookList(APIRequest apiRequest) {
        /// 处理参数
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        int user_id = Integer.parseInt(httpServletRequest.getParameter("user_id"));

        //执行查询
        List<AddressBook> addressBookList = addressBookMapper.selectList(user_id);
        if (addressBookList.size() > 0) {
            return APIUtil.getResponse(SUCCESS, addressBookList);
        } else {
            return APIUtil.selectErrorResponse("用户无地址簿信息", null);
        }
    }
}
