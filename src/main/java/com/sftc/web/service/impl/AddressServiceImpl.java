package com.sftc.web.service.impl;


import com.sftc.tools.api.*;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.AddressResolutionMapper;
import com.sftc.web.model.entity.AddressResolution;
import com.sftc.web.model.entity.Address;
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
import static com.sftc.tools.constant.ThirdPartyConstant.*;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;
    @Resource
    private AddressResolutionMapper addressResolutionMapper;

    public APIResponse addAddress(APIRequest request) {
        APIStatus status = SUCCESS;
        JSONObject paramOBJ = JSONObject.fromObject(request.getRequestParam());
        Address address = (Address) JSONObject.toBean(paramOBJ,Address.class);
        address.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            Integer user_id = TokenUtils.getInstance().getUserId();
            address.setUser_id(user_id);
            addressMapper.addAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse addAddress(Object object) {
        return null;
    }


    public APIResponse geocoderAddress(APIRequest request) {
        JSONObject resultJsonObject = new JSONObject();
        APIStatus status = SUCCESS;

        // Handle Param
        String address = (String) request.getParameter("address");
        if (address == null || address.equals("")) {
            return APIUtil.paramErrorResponse("地址不能为空");
        }
        String utf8Address = null;

        try { // url encode
            utf8Address = new String(address.getBytes("ISO-8859-1"), "UTF-8");
            address = URLEncoder.encode(utf8Address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 查库
        AddressResolution addressResolution = addressResolutionMapper.selectAddressResolution(utf8Address);
        if (addressResolution != null) {
            resultJsonObject.put("longitude", addressResolution.getLongitude());
            resultJsonObject.put("latitude", addressResolution.getLatitude());
            return APIUtil.getResponse(status, resultJsonObject);
        }

        // GET
        String geocoderUrl = MAP_GEOCODER_URL.replace("{address}", address);
        HttpGet get = new HttpGet(geocoderUrl);
        String result = APIGetUtil.get(get);
        JSONObject resObject = JSONObject.fromObject(result);

        // Result
        if ((Integer) resObject.get("status") == 0) {
            // query ok
            JSONObject locationObject = resObject.getJSONObject("result").getJSONObject("location");
            resultJsonObject.put("longitude", locationObject.get("lng"));
            resultJsonObject.put("latitude", locationObject.get("lat"));
            // 将结果存入数据库，
            addressResolutionMapper.insertAddressResolution(
                    new AddressResolution(utf8Address,
                            Double.valueOf(locationObject.get("lng").toString()),
                            Double.valueOf(locationObject.get("lat").toString()))
            );
        } else {
            status = APIStatus.SELECT_FAIL;
            return APIUtil.selectErrorResponse("解析失败", resultJsonObject);
        }

        return APIUtil.getResponse(status, resultJsonObject);
    }

    public APIResponse getAddressDistance(APIRequest request) {

        Object object = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(object);

        JSONObject sourceObject = requestObject.getJSONObject("source");
        JSONObject targetObject = requestObject.getJSONObject("target");
        double senderLong = sourceObject.getDouble("longitude");
        double senderLat = sourceObject.getDouble("latitude");
        double receiverLong =  targetObject.getDouble("longitude");
        double receiverLat =targetObject.getDouble("latitude");

        if (senderLong == 0 || receiverLong == 0 || senderLat == 0 || receiverLat == 0)
            return APIUtil.paramErrorResponse("请求体不完整");

        String from = senderLat + "," + senderLong;
        String to = receiverLat + "," + receiverLong;
        //使用 腾讯 路径规划服务
        String url = MAP_ADDRESS_DISTANCE_URL_2.replace("{from}", from).replace("{to}", to);
        String result = APIGetUtil.get(new HttpGet(url));
        JSONObject resultObject = JSONObject.fromObject(result);

        if ((Integer) resultObject.get("status") != 0)
            return APIUtil.selectErrorResponse((String) resultObject.get("message"), null);

        JSONArray routesObjects = resultObject.getJSONObject("result").getJSONArray("routes");
        JSONObject routesObject = (JSONObject) routesObjects.get(0);
        // 返回值实际上是int
        double distance = routesObject.getDouble("distance");
        //返回值为总距离（单位：米），换算成km
        distance = distance / 1000;

        Map<String, Double> resultMap = new HashMap<String, Double>();
        resultMap.put("distance", distance);
        return APIUtil.getResponse(SUCCESS, resultMap);
    }
}
