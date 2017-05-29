package com.sftc.tools.api;

import com.google.gson.Gson;
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

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.tools.api
 * @Description:
 * @date 2017/4/12
 * @Time 下午7:07
 */
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
        List<Orders> orderses = null;
        URL url = new URL(apiUrl);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("PushEnvelope-Device-Token", token);
        InputStream inputStream = connection.getInputStream();
        String old_json = IOUtils.toString(inputStream);
        JSONObject jasonObject = JSONObject.fromObject(old_json);
        Map map = (Map)jasonObject;
        String new_json = map.get("requests").toString();
        orderses = (List<Orders>) JSONArray.toList(JSONArray.fromObject(new_json), Orders.class);
        for (Orders orders : orderses) {
            System.out.println(orders.getStatus());
        }
        return orderses;
    }
}
