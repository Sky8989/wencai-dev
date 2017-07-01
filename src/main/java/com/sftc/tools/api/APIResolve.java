package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.sfmodel.Coupon;
import com.sftc.web.model.sfmodel.Orders;
import com.sftc.web.model.wechat.WechatUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class APIResolve {

    public static WechatUser getWechatJson(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        WechatUser wechatUser;
        InputStream inputStream = url.openConnection().getInputStream();
        String json = IOUtils.toString(inputStream);
        Gson gson = new Gson();
        wechatUser = gson.fromJson(json, WechatUser.class);
        return wechatUser;
    }

    public static List<Orders> getOrdersJson(String apiUrl, String token) throws Exception {
        List<Orders> orderses;
        URL url = new URL(apiUrl);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("PushEnvelope-Device-Token", token);
        InputStream inputStream = connection.getInputStream();
        String old_json = IOUtils.toString(inputStream);
        JSONObject jasonObject = JSONObject.fromObject(old_json);
        Map map = (Map) jasonObject;
        String new_json = map.get("requests").toString();
        orderses = (List<Orders>) JSONArray.toList(JSONArray.fromObject(new_json), Orders.class);
        return orderses;
    }

    public static List<Coupon> getCouponsJson(String apiUrl, String token, String method) throws Exception {
        List<Coupon> couponList;
        URL url = new URL(apiUrl);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("PushEnvelope-Device-Token", token);
        InputStream inputStream = connection.getInputStream();
        String old_json = IOUtils.toString(inputStream);
        JSONObject jasonObject = JSONObject.fromObject(old_json);
        Map map = (Map) jasonObject;
        String new_json = map.get("coupons").toString();
        couponList = (List<Coupon>) JSONArray.toList(JSONArray.fromObject(new_json), Orders.class);
        return couponList;
    }
}
