package com.sftc.tools.api;

import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpPost;


public class APIGetToken {
    private static final String REQUESTS_URL = "http://api-c.sf-rush.com/token";

    public static String getToken() {
        HttpPost post = new HttpPost(REQUESTS_URL);
        String res = AIPPost.getPost("grant_type=password&username=wcb657ca9e16094066b72362bc3046d6&password=a9d997788d0248c398d6518517703afb", post);
        JSONObject jsonObject = JSONObject.fromObject(res);

        return (String) jsonObject.get("access_token");
    }
}
