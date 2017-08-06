package com.sftc.tools.constant;

import java.util.*;

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
    public static double RANGE_NUMBER = 0.3;

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

}
