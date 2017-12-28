package com.sftc.tools.api;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sftc.web.model.others.WXUser;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderSynVO;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Administrator
 */
public class ApiResolve {

    public static WXUser getWxUserWithUrl(String apiUrl) throws Exception {

        InputStream inputStream = new URL(apiUrl).openConnection().getInputStream();
        String json = IOUtils.toString(inputStream);

        return new Gson().fromJson(json, WXUser.class);
    }

    public static List<OrderSynVO> getOrderStatusWithUrl(String apiUrl, String token) throws Exception {

        URL url = new URL(apiUrl);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("PushEnvelope-Device-Token", token);
        InputStream inputStream = connection.getInputStream();
        String json = IOUtils.toString(inputStream);
        JSONObject jsonObject = JSONObject.fromObject(json);

        return JSON.parseArray(jsonObject.get("requests").toString(), OrderSynVO.class);
    }
}
