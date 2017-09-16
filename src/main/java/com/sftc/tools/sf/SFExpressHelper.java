package com.sftc.tools.sf;

import com.sftc.tools.api.APIGetUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;

import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

public class SFExpressHelper {

    public static JSONObject getExpressDetail(String uuid, String access_token) {

        String url = SF_REQUEST_URL + "/" + uuid;
        HttpGet get = new HttpGet(url);
        get.addHeader("PushEnvelope-Device-Token", access_token);
        String res = APIGetUtil.get(get);

        return JSONObject.fromObject(res);
    }
}
