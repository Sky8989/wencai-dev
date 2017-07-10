package com.sftc.tools.sf;

import com.sftc.tools.api.APIPostUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;

import static com.sftc.tools.constant.SFConstant.SF_TOKEN_URL;

public class SFTokenHelper {

    public static String getToken() {

        HttpPost post = new HttpPost(SF_TOKEN_URL);
        String res = APIPostUtil.post("grant_type=password&username=wcb657ca9e16094066b72362bc3046d6&password=a9d997788d0248c398d6518517703afb", post);
        JSONObject jsonObject = JSONObject.fromObject(res);

        return (String) jsonObject.get("access_token");
    }
}
