package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.mapper.OrderExpressMapper;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/5/29.
 */

public class APISfDetail {
    private static String REQUESTS_URL = "http://api-dev.sf-rush.com/requests/";
    public static JSONObject sfOrderDetail(String access_token,String uuid){


        REQUESTS_URL = REQUESTS_URL + uuid;
        HttpGet get = new HttpGet(REQUESTS_URL);
        get.addHeader("PushEnvelope-Device-Token",access_token);
        String res = APIGet.getGet("", get);

       JSONObject jsonObject = JSONObject.fromObject(res);

        return jsonObject;

    }
}
