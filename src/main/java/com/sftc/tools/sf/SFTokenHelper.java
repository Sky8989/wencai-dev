package com.sftc.tools.sf;

import com.sftc.tools.api.APIPostUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;

import static com.sftc.tools.constant.SFConstant.SF_TOKEN_URL;

public class SFTokenHelper {
    /**
     * 顺丰共用access_token
     * 使用本token的接口：来往记录 好友圈 我的订单
     */
    public static String COMMON_ACCESSTOKEN = "a397RZClJVoBlWyzwRhz";
    public static String COMMON_UUID = "2c9a85895d82ebe7015d8d4c6cc11df6";

    public static String getToken() {

        HttpPost post = new HttpPost(SF_TOKEN_URL);
        String res = APIPostUtil.post("grant_type=password&username=wcb657ca9e16094066b72362bc3046d6&password=a9d997788d0248c398d6518517703afb", post);
        JSONObject jsonObject = JSONObject.fromObject(res);

        return (String) jsonObject.get("access_token");
    }
}
