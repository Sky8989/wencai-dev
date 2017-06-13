package com.sftc.tools.api;

import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpPost;




/**
 * Created by Administrator on 2017/6/14.
 */
public class APIGetToken {
    private static String REQUESTS_URL = "http://api-c-test.sf-rush.com/token";
    public static String getToken(){
        HttpPost post = new HttpPost(REQUESTS_URL);

        String res = AIPPost.getPost("grant_type=password&username=wcb657ca9e16094066b72362bc3046d6&password=a9d997788d0248c398d6518517703afb",post);
        JSONObject jsonObject = JSONObject.fromObject(res);
        String access_token =(String) jsonObject.get("access_token");

        return access_token;
    }
}
