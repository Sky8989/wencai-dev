package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.GiftCard;

public interface GiftCardService {

    APIResponse getGiftCardList(APIRequest request);
}
