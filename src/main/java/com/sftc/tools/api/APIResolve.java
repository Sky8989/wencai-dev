package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.wechat.WechatUser;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.tools.api
 * @Description:
 * @date 2017/4/12
 * @Time 下午7:07
 */
public class APIResolve {

    public static WechatUser getJson(String apiUrl) {
        String value = null;
        WechatUser wechatUser = null;
        try {
            URL url = new URL(apiUrl);
            InputStream inputStream = url.openConnection().getInputStream();
            String json = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            wechatUser = gson.fromJson(json, WechatUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wechatUser;
    }
}
