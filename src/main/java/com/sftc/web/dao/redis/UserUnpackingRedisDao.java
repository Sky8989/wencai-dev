package com.sftc.web.dao.redis;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class UserUnpackingRedisDao extends BaseRedisDao {

    private static final String DEFAULT_USER_UNPACKING_KEY = "DEFAULT_USER_UNPACKING_KEY";
    private static final String DEFAULT_USER_UNPACKING_VALUE = "DEFAULT_USER_UNPACKING_VALUE";

    public JSONArray getUserUnpacking() {
        String cache = getCache(DEFAULT_USER_UNPACKING_KEY);
        if (cache != null) {
            return JSONArray.fromObject(cache);
        }
        return null;
    }

    public void setDefaultUserUnpackingValue() {
        JSONArray defaultValue = new JSONArray();
        defaultValue.add(DEFAULT_USER_UNPACKING_VALUE);
        setCache(DEFAULT_USER_UNPACKING_KEY, new Gson().toJson(defaultValue));
    }

    public void setUserUnpacking(JSONArray value) {
        setCache(DEFAULT_USER_UNPACKING_KEY, new Gson().toJson(value));
    }

    public void removeUserUnpacking() {
        clearCache(DEFAULT_USER_UNPACKING_KEY);
    }
}
