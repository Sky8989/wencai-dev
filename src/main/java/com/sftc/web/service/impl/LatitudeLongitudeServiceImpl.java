package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.constant.LLConstant;
import com.sftc.web.service.LatitudeLongitudeService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机获取经纬度
 * Created by huxingyue on 2017/7/27.
 */
@Service("latitudeLongitudeService")
public class LatitudeLongitudeServiceImpl implements LatitudeLongitudeService {


    public APIResponse getLatitudeLongitude(APIRequest apiRequest) {

        //核对时间 限定范围时间内 才可以获取
        APIResponse apiResponse = checkTimeIsLogical();
        if (apiResponse != null) return apiResponse;

        JSONObject paramJSONObject = JSONObject.fromObject(apiRequest.getRequestParam());
        // 验参
        if (!paramJSONObject.containsKey("latitude")) {
            return APIUtil.paramErrorResponse("缺少纬度参数latitude");
        } else if (!paramJSONObject.containsKey("longitude")) {
            return APIUtil.paramErrorResponse("缺少经度参数longitude");
        }
        double latitude = paramJSONObject.getDouble("latitude");
        double longitude = paramJSONObject.getDouble("longitude");

        // 计算生成经纬度点的数量
        int GeneratedNumber = (LLConstant.MIN_LL_NUMBER + new Random().nextInt(LLConstant.MAX_LL_NUMBER));
        // 调用算法
        List<Map<String, Double>> LLResults = LLConstant.calculate3(latitude, longitude, LLConstant.RANGE_NUMBER, GeneratedNumber);
        if (LLResults == null) return APIUtil.selectErrorResponse("No_Point", null);

        return APIUtil.getResponse(APIStatus.SUCCESS, LLResults);
    }


    public APIResponse setLatitudeLongitudeConstant(APIRequest apiRequest) {
        JSONObject paramJSONObject = JSONObject.fromObject(apiRequest.getRequestParam());
        // 验参
        if (!paramJSONObject.containsKey("MAX_LL_NUMBER")) {
            return APIUtil.paramErrorResponse("param MAX_LL_NUMBER");
        } else if (!paramJSONObject.containsKey("MIN_LL_NUMBER")) {
            return APIUtil.paramErrorResponse("param MIN_LL_NUMBER");
        } else if (!paramJSONObject.containsKey("RANGE_NUMBER")) {
            return APIUtil.paramErrorResponse("param RANGE_NUMBER");
        }
        LLConstant.MAX_LL_NUMBER = paramJSONObject.getInt("MAX_LL_NUMBER");
        LLConstant.MIN_LL_NUMBER = paramJSONObject.getInt("MIN_LL_NUMBER");
        LLConstant.RANGE_NUMBER = paramJSONObject.getDouble("RANGE_NUMBER");

        if (paramJSONObject.containsKey("END_HOUR")) LLConstant.END_HOUR = paramJSONObject.getInt("END_HOUR");
        if (paramJSONObject.containsKey("BEGIN_HOUR")) LLConstant.BEGIN_HOUR = paramJSONObject.getInt("BEGIN_HOUR");


        return APIUtil.getResponse(APIStatus.SUCCESS, paramJSONObject);
    }


    //////////////////////////////private////////////////////////////////////

    /**
     * 检测获取随机点的时间是否符合逻辑
     *
     * @return APIResponse
     */
    private APIResponse checkTimeIsLogical() {
        //核对时间 限定范围时间内 才可以获取
        LocalTime now = LocalTime.now();
        int now_hour = LocalTime.now().getHour();
        if (now_hour >= LLConstant.END_HOUR || now_hour < LLConstant.BEGIN_HOUR) {
            return APIUtil.selectErrorResponse("No_Point", null);
        }
        return null;
    }
}
