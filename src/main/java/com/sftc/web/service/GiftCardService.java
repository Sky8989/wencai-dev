package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.GiftCard;

public interface GiftCardService {

    APIResponse getGiftCardList(APIRequest request);

    /**
     * CMS 系统 获取礼品卡列表 条件查询+分页
     */
    APIResponse selectList(APIRequest apiRequest) throws Exception;

    /**
     * CMS 系统 添加礼品卡信息
     */
    APIResponse addGiftCard(GiftCard giftCard) throws Exception;

    /**
     * CMS 系统 修改礼品卡信息
     */
    APIResponse updateGiftCard(GiftCard giftCard) throws Exception;

    /**
     * CMS 系统 删除礼品卡信息
     */
    APIResponse deleteGiftCard(APIRequest apiRequest);

	APIResponse save(APIRequest apiRequest) throws Exception;

	APIResponse deleteGiftCard(int id);


}
