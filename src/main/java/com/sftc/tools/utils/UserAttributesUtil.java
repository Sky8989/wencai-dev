package com.sftc.tools.utils;

import com.google.gson.Gson;
import net.sf.json.JSONObject;

/**
 * 路由状态同步工具类
 *
 * @author ： xfan
 * @date ：Create in 11:51 2017/11/29
 */
public class UserAttributesUtil {

    public static String getUserAttributesByOpenId(String openId) {
        JSONObject attributeObj = new JSONObject();
        attributeObj.put("c_wxopenid", openId);
        return new Gson().toJson(attributeObj);
    }
}
