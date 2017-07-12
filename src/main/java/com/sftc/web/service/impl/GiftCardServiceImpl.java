package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.GiftCardMapper;
import com.sftc.web.model.GiftCard;
import com.sftc.web.model.GiftCardList;
import com.sftc.web.model.Order;
import com.sftc.web.service.GiftCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftCardServiceImpl implements GiftCardService {
    @Resource
    private GiftCardMapper giftCardMapper;

    /*
    * 订单详情接口
    * */
    public APIResponse getGiftCard(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String orderSn = (String) request.getParameter("orderSn");
        Order order = giftCardMapper.giftCardDetail(orderSn);
        if (order == null) {
            status = APIStatus.GIFT_CARD_NOT_FOUND;
        }
        return APIUtil.getResponse(status, order);
    }

    public APIResponse getGiftCardList(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        List<GiftCard> giftCards = giftCardMapper.giftCardList();
        List<GiftCardList> giftCardLists = new ArrayList<GiftCardList>();

        for (GiftCard giftCard : giftCards) {
            // 遍历当前的giftCardLists数组，判断这个type的GiftCardList是否已经存在，如果存在，取出这个type的giftCardList
            GiftCardList giftCardList = null;
            for (GiftCardList tempList : giftCardLists) {
                if (tempList.getType().equals(giftCard.getType())) {
                    giftCardList = tempList;
                    break;
                }
            }

            if (giftCardList != null) { // 已存在，GiftCardList对象的giftCards数组添加giftCard
                List<GiftCard> gitCards = giftCardList.getGiftCards();
                gitCards.add(giftCard);
            } else { // 不存在，新建一个GiftCardList，进行赋值，再add进giftCardLists数组
                GiftCardList list = new GiftCardList();
                List<GiftCard> gitCards = new ArrayList<GiftCard>();
                gitCards.add(giftCard);
                list.setType(giftCard.getType());
                list.setGiftCards(gitCards);
                giftCardLists.add(list);
            }
        }

        return APIUtil.getResponse(status, giftCardLists);
    }
    /**
     * CMS 系统 获取礼品卡列表 条件查询+分页
     * @param apiRequest
     * @return APIResponse
     */
    public APIResponse selectList(APIRequest apiRequest) {

        return null;
    }
}
