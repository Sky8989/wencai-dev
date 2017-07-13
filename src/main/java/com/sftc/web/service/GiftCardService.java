package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 礼品卡操作接口
 * @date 2017/4/25
 * @Time 上午10:56
 */
public interface GiftCardService {
    APIResponse getGiftCard(APIRequest request);
    APIResponse getGiftCardList(APIRequest request);

    /**
     * CMS 系统 获取礼品卡列表 条件查询+分页
     * @param apiRequest
     * @return
     */
    APIResponse selectList(APIRequest apiRequest);
}
