package com.sftc.web.dao.redis;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 同城专送Redis
 *
 * @author ： CatalpaFlat
 * @date ：Create in 10:29 2017/11/24
 */
@Component
public class CityExpressRedisDao extends BaseRedisDao {

    private static final String CITY_EXPRESS_KEY = "CITY_EXPRESS_KEY";

    /**
     * 获取缓存同城列表
     *
     * @return 同城列表json
     */
    public JSONArray getCityExpresssCache() {
        String cache = getCache(CITY_EXPRESS_KEY);
        if (StringUtils.isBlank(cache)) {
            return null;
        }
        return JSONArray.fromObject(cache);
    }

    /**
     * 设置同城列表缓存
     *
     * @param jsonObject 分组封装好的同城列表对象
     */
    public void setCityExpresssCache(JSONArray jsonObject) {
        setCache(CITY_EXPRESS_KEY, new Gson().toJson(jsonObject));
    }

    /**
     * 清空同城列表缓存
     */
    public void removeCityExpresssCache() {
        clearCache(CITY_EXPRESS_KEY);
    }
}
