package com.sftc.web.dao.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.web.model.entity.SystemLabel;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserLabelsRedisDao extends BaseRedisDao {

    private static final String USER_LABELS_KEY = "USER_LABELS_KEY";
    private static final String SYSTEM_LABELS_KEY = "SYSTEM_LABELS_KEY";

    /**
     * 获取好友联系人标签
     *
     * @param user_contact_id 好友联系人表id
     * @return 分组封装好的标签结果对象
     */
    public JSONObject getUserContactLabels(int user_contact_id) {
        String cache = getCache(USER_LABELS_KEY + user_contact_id);
        if (StringUtils.isBlank(cache)) return null;

        return JSONObject.fromObject(cache);
    }

    /**
     * 获取系统标签
     *
     * @return 系统标签
     */
    public List<SystemLabel> getSystemLabels() {
        String cache = getCache(SYSTEM_LABELS_KEY);
        if (StringUtils.isBlank(cache)) return null;

        return new Gson().fromJson(cache, new TypeToken<List<SystemLabel>>() {
        }.getType());
    }

    /**
     * 设置好友联系人标签缓存
     *
     * @param user_contact_id 好友联系人表id
     * @param jsonObject      分组封装好的标签结果对象
     */
    public void setUserContactLabelsCache(int user_contact_id, JSONObject jsonObject) {
        setCache(USER_LABELS_KEY + user_contact_id, new Gson().toJson(jsonObject));
    }

    /**
     * 设置系统标签缓存
     *
     * @param systemLabels 系统标签
     */
    public void setSystemLabelsCache(List<SystemLabel> systemLabels) {
        setCache(SYSTEM_LABELS_KEY, new Gson().toJson(systemLabels));
    }

    /**
     * 清空好友联系人标签缓存
     *
     * @param user_contact_id 好友联系人表id
     */
    public void removeUserContactLabelsCache(int user_contact_id) {
        clearCache(USER_LABELS_KEY + user_contact_id);
    }

    /**
     * 清空系统标签缓存
     */
    public void removeSystemLabelsCache() {
        clearCache(SYSTEM_LABELS_KEY);
    }
}
