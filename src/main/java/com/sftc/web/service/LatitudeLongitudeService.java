package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * 随机获取经纬度
 * Created by huxingyue on 2017/7/27.
 */
public interface LatitudeLongitudeService {
    /**
     * 获取随机经纬度
     *
     * @param apiRequest
     * @return
     */
    APIResponse getLatitudeLongitude(APIRequest apiRequest);

    /**
     * 设置经纬度接口的相关参数
     *
     * @param apiRequest
     * @return
     */
    APIResponse setLatitudeLongitudeConstant(APIRequest apiRequest);
}
