package com.sftc.tools.sf;

import com.sftc.tools.api.ApiGetUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;

import static com.sftc.tools.constant.SfConstant.SF_REQUEST_URL;

/**
 * @author Administrator
 */
public class SfExpressHelper {

    public static JSONObject getExpressDetail(String uuid, String accessToken) {

        String url = SF_REQUEST_URL + "/" + uuid;
        HttpGet get = new HttpGet(url);
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);

        return JSONObject.fromObject(res);
    }
}
