package com.sftc.web.dao.redis;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 价格说明
 *
 * @author ： CatalpaFlat
 * @date ：Create in 15:39 2017/11/25
 */
@Component
public class PriceExplainRedisDao extends BaseRedisDao {

    private static final String PRICE_EXPLAIN_KEY = "PRICE_EXPLAIN_KEY";
    /**
     * 获取价格说明列表
     *
     * @return 价格说明json
     */
    public JSONArray getCityExpresssCache() {
        String cache = getCache(PRICE_EXPLAIN_KEY);
        if (StringUtils.isBlank(cache)) {
            return null;
        }
        return JSONArray.fromObject(cache);
    }

    /**
     * 设置价格说明缓存
     *
     * @param jsonObject 分组封装好的价格说明
     */
    public void setCityExpresssCache(JSONArray jsonObject) {
        setCache(PRICE_EXPLAIN_KEY, new Gson().toJson(jsonObject));
    }

    /**
     * 清空价格说明缓存
     */
    public void removeCityExpresssCache() {
        clearCache(PRICE_EXPLAIN_KEY);
    }
}
