package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.GiftCardMapper;
import com.sftc.web.model.dto.GiftCardListDTO;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.service.GiftCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
@Service
public class GiftCardServiceImpl implements GiftCardService {
    @Resource
    private GiftCardMapper giftCardMapper;

    /**
     * 获取所有礼品卡
     * @param request
     * @return 礼品卡信息
     */
    public APIResponse getGiftCardList(APIRequest request) {

        List<GiftCard> giftCards = giftCardMapper.giftCardList();
        List<GiftCardListDTO> giftCardLists = new ArrayList<GiftCardListDTO>();

        for (GiftCard giftCard : giftCards) {
            // 遍历当前的giftCardLists数组，判断这个type的GiftCardList是否已经存在，如果存在，取出这个type的giftCardList
            GiftCardListDTO giftCardList = null;
            for (GiftCardListDTO tempList : giftCardLists) {
                if (tempList.getType().equals(giftCard.getType())) {
                    giftCardList = tempList;
                    break;
                }
            }
            if (giftCardList != null) { // 已存在，GiftCardList对象的giftCards数组添加giftCard
                List<GiftCard> gitCards = giftCardList.getGiftCards();
                gitCards.add(giftCard);
            } else { // 不存在，新建一个GiftCardList，进行赋值，再add进giftCardLists数组
                GiftCardListDTO list = new GiftCardListDTO();
                List<GiftCard> gitCards = new ArrayList<GiftCard>();
                gitCards.add(giftCard);
                list.setType(giftCard.getType());
                list.setGiftCards(gitCards);
                giftCardLists.add(list);
            }
        }
        return APIUtil.getResponse(SUCCESS, giftCardLists);
    }
}
