package com.sftc.web.service.impl;


import com.sftc.tools.api.*;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.AddressResolutionMapper;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressResolution;
import com.sftc.web.model.vo.swaggerRequest.DistanceRequestVO;
import com.sftc.web.model.vo.swaggerRequest.SourceDistanceVO;
import com.sftc.web.model.vo.swaggerRequest.TargetDistanceVO;
import com.sftc.web.service.AddressService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_ADDRESS_DISTANCE_URL_2;
import static com.sftc.tools.constant.ThirdPartyConstant.MAP_GEOCODER_URL;

/**
 * @author Administrator
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;
    @Resource
    private AddressResolutionMapper addressResolutionMapper;

    @Override
    public ApiResponse addAddress(Address address) {
        ApiStatus status = SUCCESS;
        address.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            String userUUID = TokenUtils.getInstance().getUserUUID();
            address.setUser_uuid(userUUID);
            addressMapper.addAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = ApiStatus.SUBMIT_FAIL;
        }
        return ApiUtil.getResponse(status, null);
    }

    @Override
    public ApiResponse consigneeAddress() {
        ApiStatus status = ApiStatus.SELECT_FAIL;
        String userUUId = TokenUtils.getInstance().getUserUUID();
        List<Address> addressList = new ArrayList<>();
        if (StringUtils.isNotBlank(userUUId)) {
            try {
                addressList = addressMapper.addressDetail(userUUId);
                if (addressList == null) {
                    return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该用户暂无地址信息");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            status = SUCCESS;
        }
        return ApiUtil.getResponse(status, addressList);
    }

    @Override
    public ApiResponse editAddress(Address address) {
        ApiStatus status = SUCCESS;
        try {
            String userUUID = TokenUtils.getInstance().getUserUUID();
            address.setUser_uuid(userUUID);
            address.setCreate_time(Long.toString(System.currentTimeMillis()));
            addressMapper.editeAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = ApiStatus.SUBMIT_FAIL;
        }
        return ApiUtil.getResponse(status, null);
    }

    @Override
    public ApiResponse deleteAddress(ApiRequest request) {
        ApiStatus status = SUCCESS;
        String id = request.getParameter("id").toString();
        if (id != null) {
            try {
                addressMapper.deleteAddress(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
                status = ApiStatus.SUBMIT_FAIL;
            }
        }
        return ApiUtil.getResponse(status, null);
    }

    @Override
    public ApiResponse geocoderAddress(String address) {
        JSONObject resultJsonObject = new JSONObject();
        ApiStatus status = SUCCESS;

        // Handle Param
        if (StringUtils.isEmpty(address)) {
            {
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址不能为空");
            }
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
            return ApiUtil.getResponse(status, resultJsonObject);
        }

        // GET
        String geocoderUrl = MAP_GEOCODER_URL.replace("{address}", address);
        HttpGet get = new HttpGet(geocoderUrl);
        String result = ApiGetUtil.get(get);
        JSONObject resObject = JSONObject.fromObject(result);

        // Result
        if ((Integer) resObject.get(CustomConstant.STATUS) == 0) {
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
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "解析失败", resultJsonObject);
        }

        return ApiUtil.getResponse(status, resultJsonObject);
    }

    @Override
    public ApiResponse getAddressDistance(DistanceRequestVO distanceRequestVO) {


        SourceDistanceVO sourceDistanceVO = distanceRequestVO.getSource();
        TargetDistanceVO targetDistanceVO = distanceRequestVO.getTarget();
        double senderLong = sourceDistanceVO.getLongitude();
        double senderLat = sourceDistanceVO.getLatitude();
        double receiverLong = targetDistanceVO.getLongitude();
        double receiverLat = targetDistanceVO.getLatitude();

        if (senderLong == 0 || receiverLong == 0 || senderLat == 0 || receiverLat == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "请求体不完整");
        }

        String from = senderLat + "," + senderLong;
        String to = receiverLat + "," + receiverLong;
        //使用 腾讯 路径规划服务
        String url = MAP_ADDRESS_DISTANCE_URL_2.replace("{from}", from).replace("{to}", to);
        String result = ApiGetUtil.get(new HttpGet(url));
        JSONObject resultObject = JSONObject.fromObject(result);

        if ((Integer) resultObject.get(CustomConstant.STATUS) != 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), (String) resultObject.get("message"));
        }

        JSONArray routesObjects = resultObject.getJSONObject("result").getJSONArray("routes");
        JSONObject routesObject = (JSONObject) routesObjects.get(0);
        // 返回值实际上是int
        double distance = routesObject.getDouble("distance");
        //返回值为总距离（单位：米），换算成km
        distance = distance / 1000;

        Map<String, Double> resultMap = new HashMap<>(1, 1);
        resultMap.put("distance", distance);
        return ApiUtil.getResponse(SUCCESS, resultMap);
    }

}
