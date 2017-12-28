package com.sftc.web.dao.redis;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 服务电话
 *
 * @author ： CatalpaFlat
 * @date ：Create in 15:39 2017/11/25
 */
@Component
public class ServicePhoneRedisDao extends BaseRedisDao {

    private static final String SERVICE_PHONE_KEY = "SERVICE_PHONE_KEY";
    /**
     * 获取服务电话列表
     *
     * @return 服务电话json
     */
    public JSONArray getServicePhoneCache() {
        String cache = getCache(SERVICE_PHONE_KEY);
        if (StringUtils.isBlank(cache)) {
            return null;
        }
        return JSONArray.fromObject(cache);
    }

    /**
     * 设置价服务电话缓存
     *
     * @param jsonObject 分组封装好的服务电话
     */
    public void setServicePhoneCache(JSONArray jsonObject) {
        setCache(SERVICE_PHONE_KEY, new Gson().toJson(jsonObject));
    }

    /**
     * 清空服务电话缓存
     */
    public void removeServicePhoneCache() {
        clearCache(SERVICE_PHONE_KEY);
    }
}
