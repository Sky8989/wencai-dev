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
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
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

    @Override
    public APIResponse deleteAddressBook(APIRequest apiRequest) {

        return null;
    }

    @Override
    public APIResponse updateAddressBook(APIRequest apiRequest) {
        // 修改地址时 改变创建时间
        return null;
    }

    @Override
    public APIResponse selectAddressBookList(APIRequest apiRequest) {
        apiRequest.getParameter("user_id");
        return null;
    }
}
