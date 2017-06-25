package com.sftc.tools.api;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;


public class APISfDetail {
    private static final String REQUESTS_URL = "http://api-dev.sf-rush.com/requests";

    public static JSONObject sfDetail(String uuid, String access_token) {
        String url = REQUESTS_URL + "/" + uuid;
        HttpGet get = new HttpGet(url);
        get.addHeader("PushEnvelope-Device-Token", access_token);
        String res = APIGet.getGet(get);

        return JSONObject.fromObject(res);
    }
}
