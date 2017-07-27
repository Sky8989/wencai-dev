package com.sftc.tools.constant;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.PI;

/**
 * Latitude and longitude 经纬度位置获取服务的相关变量和方法
 * Created by huxingyue on 2017/7/27.
 */
public class LLConstant {
    // 最大生成数量
    public static int MAX_LL_NUMBER = 5;
    // 最小生成数量
    public static int MIN_LL_NUMBER = 1;
    // 生成范围 单位KM
    public static int RANGE_NUMBER = 3;

    public static List<Double[]> calculate(double startlat, double startlon, double maxdist, int GeneratedNumber) {

        //将所有纬度和经度转换为弧度。
        startlat = startlat * PI / 180;
        startlon = startlon * PI / 180;

        //地球的平均半径
        double radiusEarth = 6372.796924;
        //将最大距离转换为弧度。
        maxdist = maxdist / radiusEarth;

        List<Double[]> gpsDataList = new LinkedList<Double[]>();
        for (int i = 0; i < GeneratedNumber; i++) {
            // 生成两个随机数 0 到1 的double
            double rand1 = new Random().nextDouble();
            double rand2 = new Random().nextDouble();

            // 计算从0到maxdist缩放的随机距离
            double dist = Math.acos((rand1 * (Math.cos(maxdist) - 1) + 1));
            double brg = 2 * PI * rand2;

            double lat = Math.asin(Math.sin(startlat) * Math.cos(dist) + Math.cos(startlat) * Math.sin(dist) * Math.cos(brg));
            double lon = startlon + Math.atan2(Math.sin(brg) * Math.sin(dist) * Math.cos(startlat), Math.cos(dist) * Math.sin(startlat) * Math.sin(lat));
            if (lon < -PI) lon = lon + 2 * PI;
            if (lon > PI) lon = lon - 2 * PI;

            Double[] doubles = new Double[2];
            // 弧度转换成经纬度
            doubles[0] = lat * 180 / PI;
            doubles[1] = lon * 180 / PI;
            gpsDataList.add(doubles);
        }
        return gpsDataList;
    }

}
