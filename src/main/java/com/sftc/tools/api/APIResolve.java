package com.sftc.tools.api;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

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

    public static String getJson(String apiUrl, String key) {
        String value = null;
        try {
            URL url = new URL(apiUrl);
            InputStream inputStream = url.openConnection().getInputStream();
            String json = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            Map<String, String> maps = gson.fromJson(json, Map.class);
            value = maps.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
