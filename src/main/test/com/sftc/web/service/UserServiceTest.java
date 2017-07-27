package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.PI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-dao.xml"})
public class UserServiceTest {

    @Resource
    private UserService userService;


    APIRequest request;

    @Before
    public void setUp() throws Exception {
        request = new APIRequest();
    }

    @Test
    public void wechatLogin() throws Exception {

        request.setAttribute("js_code", "003GdtOj0jwU7k1UelPj01NyOj0GdtOw");
        // APIResponse response = userService.login(request);
        // Assert.assertTrue(response.getMessage(), response.getState().equals("40029"));
    }

    // @Test
    // public void login() throws Exception {
    //
    //     request.setAttribute("user_phone", "skm");
    //     request.setAttribute("user_password", "123");
    //     APIResponse response = userService.login(request);
    //     Assert.assertTrue(response.getMessage(), response.getState().equals("00001"));
    //
    //     request.setAttribute("user_phone", "skm");
    //     request.setAttribute("user_password", "12");
    //     response = userService.login(request);
    //     Assert.assertTrue(response.getMessage(), response.getState().equals("00002"));
    //
    //     request.setAttribute("user_phone", "sk");
    //     request.setAttribute("user_password", "222");
    //     response = userService.login(request);
    //     Assert.assertTrue(response.getMessage(), response.getState().equals("00003"));
    // }
    @Test
    public void register() throws Exception {
        Token order = userService.getToken(85);
        System.out.println(order.getAccess_token());
    }

    @Test
    public void calculateTest() {
        calculate(39.90419989999999, 116.40739630000007, 5);

    }

    public void calculate(double startlat, double startlon, double maxdist) {


        //将所有纬度和经度转换为弧度。
        startlat = startlat * PI / 180;
        startlon = startlon * PI / 180;

        //地球的平均半径
        double radiusEarth = 6372.796924;
        //将最大距离转换为弧度。
        maxdist = maxdist / radiusEarth;

        List<GPSData> gpsDataList = new LinkedList<GPSData>();
        for (int i = 0; i < 5; i++) {
            // 定义两个随机数 0 到1 的
            double rand1 = new Random().nextDouble();
            double rand2 = new Random().nextDouble();
            // 计算从0到maxdist缩放的随机距离
            double temp = (rand1 * (Math.acos(maxdist) - 1) + 1);
            double dist = Math.acos((rand1 * (Math.cos(maxdist) - 1) + 1));
            double brg = 2 * PI * rand2;
            double lat = Math.asin(Math.sin(startlat) * Math.cos(dist) + Math.cos(startlat) * Math.sin(dist) * Math.cos(brg));
            double lon = startlon + Math.atan2(Math.sin(brg) * Math.sin(dist) * Math.cos(startlat), Math.cos(dist) * Math.sin(startlat) * Math.sin(lat));
            if (lon < -PI) lon = lon + 2 * PI;
            if (lon > PI) lon = lon - 2 * PI;

            GPSData gpsData = new GPSData();
            // 弧度转换成经纬度
            gpsData.setLat(lat * 180 / PI);
            gpsData.setLon(lon * 180 / PI);
            gpsDataList.add(gpsData);
            System.out.println(gpsData.toString());
        }
    }

    private class GPSData {
        double lat;
        double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        @Override
        public String toString() {
            return "GPSData{" +
                    "lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }
}

