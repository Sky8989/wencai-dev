package com.sftc.tools.utils;

import com.sftc.tools.api.ApiGetUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.web.model.vo.swaggerRequest.SourceDistanceVO;
import com.sftc.web.model.vo.swaggerRequest.TargetDistanceVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;
import java.util.Map;

import static com.sftc.tools.constant.ThirdPartyConstant.MAP_ADDRESS_DISTANCE_URL_2;

/**
 * 地址距离计算工具类
 *
 * @author ： xfan
 * @date ：Create in 11:51 2017/11/29
 */
public class AddressDistanceUtil {

    public static Double getAddressDistance(SourceDistanceVO sourceDistanceVO, TargetDistanceVO targetDistanceVO) throws Exception {

        double senderLong = sourceDistanceVO.getLongitude();
        double senderLat = sourceDistanceVO.getLatitude();
        double receiverLong = targetDistanceVO.getLongitude();
        double receiverLat = targetDistanceVO.getLatitude();

        if (senderLong == 0 || receiverLong == 0 || senderLat == 0 || receiverLat == 0) {
            throw new Exception("请求体不完整");
        }

        String from = senderLat + "," + senderLong;
        String to = receiverLat + "," + receiverLong;
        //使用 腾讯 路径规划服务
        String url = MAP_ADDRESS_DISTANCE_URL_2.replace("{from}", from).replace("{to}", to);
        String result = ApiGetUtil.get(new HttpGet(url));
        JSONObject resultObject = JSONObject.fromObject(result);

        if ((Integer) resultObject.get(CustomConstant.STATUS) != 0) {
            throw new Exception((String) resultObject.get("message"));
        }

        JSONArray routesObjects = resultObject.getJSONObject("result").getJSONArray("routes");
        JSONObject routesObject = (JSONObject) routesObjects.get(0);
        // 返回值实际上是int
        Double distance = routesObject.getDouble("distance");
        //返回值为总距离（单位：米），换算成km
        distance = distance / 1000;
        return distance;
    }

    public static Map<String, Object> getCoordinate(JSONObject sourceObj, JSONObject targetObj) {
        SourceDistanceVO sourceDistanceVO = new SourceDistanceVO();
        TargetDistanceVO targetDistanceVO = new TargetDistanceVO();
        sourceDistanceVO.setLatitude(sourceObj.getDouble("latitude"));
        sourceDistanceVO.setLongitude(sourceObj.getDouble("longitude"));
        targetDistanceVO.setLatitude(targetObj.getDouble("latitude"));
        targetDistanceVO.setLongitude(targetObj.getDouble("longitude"));
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put("sourceDistance", sourceDistanceVO);
        map.put("targetDistance", targetDistanceVO);
        return map;
    }
}
