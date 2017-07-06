package com.sftc.web.service.impl;


import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.model.Address;
import com.sftc.web.model.sfmodel.SFServiceAddress;
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
import java.util.concurrent.Semaphore;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_ADDRESS_DISTANCE_URL;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_GEOCODER_URL;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    /**
     * 信号量
     * 腾讯位置服务暂时只能5个并发数/1秒，所以保险起见设4个信号量，在内部休眠1.5秒
     */
    private final Semaphore semaphore = new Semaphore(4);

    public APIResponse addAddress(Address address) {
        APIStatus status = SUCCESS;
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
            status = SUCCESS;
        }
        return APIUtil.getResponse(status, addressList);
    }

    public APIResponse editAddress(Address address) {
        APIStatus status = SUCCESS;
        try {
            addressMapper.editeAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse deleteAddress(APIRequest request) {
        APIStatus status = SUCCESS;
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
            return APIUtil.paramErrorResponse("地址不能为空");
        }

//        try { // url encode
//            address = new String(address.getBytes("ISO-8859-1"), "UTF-8");
//            address = URLEncoder.encode(address, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        try { // 请求许可
            semaphore.acquire();
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // GET
        String geocoderUrl = MAP_GEOCODER_URL.replace("{address}", address);
        HttpGet get = new HttpGet(geocoderUrl);
        String result = APIGetUtil.get(get);
        JSONObject resObject = JSONObject.fromObject(result);

        // Result
        APIStatus status = SUCCESS;
        JSONObject resultJsonObject = new JSONObject();
        if ((Integer) resObject.get("status") == 0) {
            // query ok
            JSONObject locationObject = resObject.getJSONObject("result").getJSONObject("location");
            resultJsonObject.put("longitude", locationObject.get("lng"));
            resultJsonObject.put("latitude", locationObject.get("lat"));
        } else {
            status = APIStatus.SELECT_FAIL;
        }

        // 释放许可
        semaphore.release();

        return APIUtil.getResponse(status, resultJsonObject);
    }

    public APIResponse getAddressDistance(APIRequest request) {

        Object object = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(object);

        JSONObject sourceObject = requestObject.getJSONObject("source");
        JSONObject targetObject = requestObject.getJSONObject("target");
        double senderLong = (Double) sourceObject.get("longitude");
        double senderLat = (Double) sourceObject.get("latitude");
        double receiverLong = (Double) targetObject.get("longitude");
        double receiverLat = (Double) targetObject.get("latitude");

        if (senderLong == 0 || receiverLong == 0 || senderLat == 0 || receiverLat == 0)
            return APIUtil.paramErrorResponse("请求体不完整");

        String from = senderLat + ";" + senderLong;
        String to = receiverLat + ";" + receiverLong;

        String url = MAP_ADDRESS_DISTANCE_URL.replace("{from}", from).replace("{to}", to);
        String result = APIGetUtil.get(new HttpGet(url));
        JSONObject resultObject = JSONObject.fromObject(result);

        if ((Integer) resultObject.get("status") != 0)
            return APIUtil.selectErrorResponse((String) resultObject.get("message"), null);

        JSONArray elementObjects = resultObject.getJSONObject("result").getJSONArray("elements");
        JSONObject elementObject = (JSONObject) elementObjects.get(0);
        double distance = (Double) elementObject.get("distance");

        Map<String, Double> resultMap = new HashMap<String, Double>();
        resultMap.put("distance", distance);
        return APIUtil.getResponse(SUCCESS, resultMap);
    }
}
