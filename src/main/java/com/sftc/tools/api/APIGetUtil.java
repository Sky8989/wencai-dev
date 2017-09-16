package com.sftc.tools.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class APIGetUtil {

    public static String get(HttpGet get) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String resultStr = "";
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultStr;
    }
}

