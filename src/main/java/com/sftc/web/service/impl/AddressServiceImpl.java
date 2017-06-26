package com.sftc.web.service.impl;


import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    private static final String MAP_GEOCODER_URL = "http://apis.map.qq.com/ws/geocoder/v1/?address=";
    private static final String MAP_GEOCODER_KEY = "ZZABZ-SLCWG-HV4Q4-IJYLH-LLBD6-V3FPR";

    @Resource
    private AddressMapper addressMapper;

    public APIResponse addAddress(Address address) {
        APIStatus status = APIStatus.SUCCESS;
        address.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            addressMapper.addAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse consigneeAddress(APIRequest request) {
        APIStatus status = APIStatus.SELECT_FAIL;
        String id = request.getParameter("user_id").toString();
        List<Address> addressList = new ArrayList<Address>();
        if (id != null) {
            try {
                addressList = addressMapper.addressDetail(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            status = APIStatus.SUCCESS;
        }
        return APIUtil.getResponse(status, addressList);
    }

    public APIResponse editAddress(Address address) {
        APIStatus status = APIStatus.SUCCESS;
        try {
            addressMapper.editeAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse deleteAddress(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String id = request.getParameter("id").toString();
        if (id != null) {
            try {
                addressMapper.deleteAddress(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
                status = APIStatus.SUBMIT_FAIL;
            }
        }
        return APIUtil.getResponse(status, null);

    }


    public APIResponse addAddress(Object object) {
        return null;
    }


    public APIResponse geocoderAddress(APIRequest request) {

        // Handle Param
        String address = (String) request.getParameter("address");
        if (address == null || address.equals("")) {
            return APIUtil.errorResponse("地址不能为空");
        }

        // GET
        String geocoderUrl = MAP_GEOCODER_URL + address + "&key=" + MAP_GEOCODER_KEY;
        HttpGet get = new HttpGet(geocoderUrl);
        String result = APIGet.getGet(get);
        JSONObject resObject = JSONObject.fromObject(result);

        // Result
        APIStatus status = APIStatus.SUCCESS;
        JSONObject resultJsonObject = new JSONObject();
        if ((Integer) resObject.get("status") == 0) {
            // query ok
            JSONObject locationObject = resObject.getJSONObject("result").getJSONObject("location");
            resultJsonObject.put("longitude", locationObject.get("lng"));
            resultJsonObject.put("latitude", locationObject.get("lat"));
        } else {
            status = APIStatus.SELECT_FAIL;
        }

        return APIUtil.getResponse(status, resultJsonObject);
    }
}
