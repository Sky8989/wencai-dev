package com.sftc.tools.constant;

import com.sftc.tools.api.APIGetUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;

import java.util.*;

import static com.sftc.tools.constant.ThirdPartyConstant.MAP_REGEOCODER_URL;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.sin;

/**
 * Latitude and longitude 经纬度位置获取服务的相关变量和方法
 * Created by huxingyue on 2017/7/27.
 */
public class LLConstant {
    // 最大生成数量
    public static int MAX_LL_NUMBER = 6;
    // 最小生成数量
    public static int MIN_LL_NUMBER = 3;
    // 生成范围 单位KM
    public static double RANGE_NUMBER = 1;
    //生成点之间的距离 单位KM
    public static double DISTANCE_BETWEEN_PONINT = RANGE_NUMBER / 3;

    // 每天开始时间 用于界定生成随机点的时间
    public static int BEGIN_HOUR = 6;
    // 每天结束时间
    public static int END_HOUR = 21;
    public static List<Map<String, Double>> calculate(double startlat, double startlon, double maxdist, int GeneratedNumber) {

        //将所有纬度和经度转换为弧度。
        startlat = startlat * PI / 180;
        startlon = startlon * PI / 180;

        //地球的平均半径
        double radiusEarth = 6372.796924;
        //将最大距离转换为弧度。
        maxdist = maxdist / radiusEarth;

        List<Map<String, Double>> gpsDataList = new LinkedList<Map<String, Double>>();
        for (int i = 0; i < GeneratedNumber; i++) {
            // 生成两个随机数 0 到1 的double
            double rand1 = new Random().nextDouble();
            double rand2 = new Random().nextDouble();

            // 计算从0到maxdist缩放的随机距离
            double dist = Math.acos((rand1 * (Math.cos(maxdist) - 1) + 1));
            double brg = 2 * PI * rand2;

            double lat = asin(Math.sin(startlat) * Math.cos(dist) + Math.cos(startlat) * Math.sin(dist) * Math.cos(brg));
//            double lon = startlon + Math.atan2(Math.sin(brg) * Math.sin(dist) * Math.cos(startlat), Math.cos(dist) * Math.sin(startlat) * Math.sin(lat));
            //减少偏移量
            double lon = startlon + 0.33 * Math.atan2(Math.sin(brg) * Math.sin(dist) * Math.cos(startlat), Math.cos(dist) * Math.sin(startlat) * Math.sin(lat));
            if (lon < -PI) {
                lon = lon + 2 * PI;
            }
            if (lon > PI) {
                lon = lon - 2 * PI;
            }

            // 弧度转换成经纬度
            Map<String, Double> map = new HashMap<String, Double>();
            map.put("latitude", lat * 180 / PI);
            map.put("longitude", lon * 180 / PI);
            gpsDataList.add(map);
        }
        return gpsDataList;
    }

    public static List<Map<String, Double>> calculate2(double startlat, double startlon, double maxdist, int GeneratedNumber) {
        double northlimit = startlat - 0.01;
        double southlimit = startlat + 0.01;
        double eastlimit = startlat + 0.05;
        double westlimit = startlat - 0.05;
        //将所有纬度和经度转换为弧度。
        startlat = startlat * PI / 180;
        startlon = startlon * PI / 180;
        northlimit = northlimit * PI / 180;
        southlimit = southlimit * PI / 180;
        eastlimit = eastlimit * PI / 180;
        westlimit = westlimit * PI / 180;
        List<Map<String, Double>> gpsDataList = new LinkedList<Map<String, Double>>();

        for (int i = 0; i < GeneratedNumber; i++) {
            // 生成两个随机数 0 到1 的double
            double rand1 = new Random().nextDouble();
            double rand2 = new Random().nextDouble();

            double lat = asin(rand1 * (sin(northlimit) - sin(southlimit)) + sin(southlimit));
            // 找到矩形区域的宽度。
            double width = eastlimit - westlimit;
            double lon = westlimit + width * rand2;
            if (lon < -PI) {
                lon = lon + 2 * PI;
            }
            if (lon > PI) {
                lon = lon - 2 * PI;
            }

            // 弧度转换成经纬度
            Map<String, Double> map = new HashMap<String, Double>();
            map.put("latitude", lat * 180 / PI);
            map.put("longitude", lon * 180 / PI);
            gpsDataList.add(map);
        }
        return gpsDataList;
    }

    public static List<Map<String, Double>> calculate3(double startlat, double startlon, double maxdist, int GeneratedNumber) {

        String poi_options = "address_format=short;radius={radius};page_size=20;page_index=1;policy=3;category=便利店,超市,房产小区";
        int radius = (int) (maxdist * 1000);
        poi_options = poi_options.replace("{radius}", String.valueOf(radius));
        String url = MAP_REGEOCODER_URL;
        //处理请求参数

        url = url.replace("{poi_options}", poi_options);
        url = url.replace("{location}", startlat + "," + startlon);


        HttpGet httpGet = new HttpGet(url);
        String resultStr = APIGetUtil.get(httpGet);
        JSONObject resultObject = JSONObject.fromObject(resultStr);
        if (!"0".equals(resultObject.getString("status"))) return null;
        JSONArray pois = resultObject.getJSONObject("result").getJSONArray("pois");

        List<Map<String, Double>> gpsDataList = new LinkedList<Map<String, Double>>();


        for (int i = 0; i < pois.size(); i++) {
            boolean flag = true;
            // 弧度转换成经纬度
            Map<String, Double> map = new HashMap<String, Double>(1, 2);

            double lat = pois.getJSONObject(i).getJSONObject("location").getDouble("lat");
            double lng = pois.getJSONObject(i).getJSONObject("location").getDouble("lng");

            //对生成的点的距离进行限制
            for (Map<String, Double> map1 : gpsDataList) {

                double distance = distance2(lng, lat, map1.get("longitude"), map1.get("latitude")) * 1000;
                //maxdist单位是km distance单位是m
                if (distance < DISTANCE_BETWEEN_PONINT * 1000) flag = false;
            }
            //让内层控制外层循环
            if (!flag) continue;

            //如果通过距离判断器，则加入待用列表
            map.put("latitude", pois.getJSONObject(i).getJSONObject("location").getDouble("lat"));
            map.put("longitude", pois.getJSONObject(i).getJSONObject("location").getDouble("lng"));
            gpsDataList.add(map);

            //数量足够，则跳出循环
            if (gpsDataList.size() == GeneratedNumber) break;
        }
        return gpsDataList;
    }

    private static double distance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static double distance2(double lon1, double lat1, double lon2, double lat2) {
        double EARTH_RADIUS = 6378.137;//赤道半径(单位m)
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }

}
