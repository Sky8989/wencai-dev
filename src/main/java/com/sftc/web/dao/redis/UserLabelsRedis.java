package com.sftc.web.dao.redis;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by CatalpaFlat on 2017/11/7.
 */
@Component
public class UserLabelsRedis extends BaseRedisDao {

    public static final String USER_LABELS_KEY = "USER_LABELS_KEY";

    public JSONArray getUserLabelsFromRedis() {
        String cache = getCache(USER_LABELS_KEY);
        if (StringUtils.isBlank(cache))
            return null;
        return JSONArray.fromObject(cache);
    }

    public void setUserLabelsFromRedis(String value) {
        setCache(USER_LABELS_KEY, value);
    }

    public void removeUserLabelsFromRedis() {
        clearCache(USER_LABELS_KEY);
    }
}
