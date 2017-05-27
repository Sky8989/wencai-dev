package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.sfmodel.Orders;
import com.sftc.web.model.wechat.WechatUser;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.*;

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

    public static Orders getOrdersJson(String apiUrl) {
        Orders orders = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            // connection.setRequestProperty("PushEnvelope-Device-Token", token);
            InputStream inputStream = connection.getInputStream();
            String json = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            orders = gson.fromJson(json, Orders.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
