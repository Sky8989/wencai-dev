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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description: 礼品卡操作接口实现
 * @date 2017/4/25
 * @Time 上午10:56
 */
@Service
public class GiftCardServiceImpl implements GiftCardService {
    @Resource
    private GiftCardMapper giftCardMapper;
    @Override
    /*
    * 订单详情接口
    * */
    public APIResponse getGiftCard(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String orderSn = (String)request.getParameter("orderSn");
        Order order= giftCardMapper.giftCardDetail(orderSn);
        if(order==null){
            status = APIStatus.GIFT_CARD_NOT_FOUND;
        }
        return  APIUtil.getResponse(status, order);
    }

    @Override
    public APIResponse getGiftCardList(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        GiftCardList giftCardList1 = new GiftCardList();
        List<GiftCard> giftCardList= giftCardMapper.giftCardList();
        giftCardList1.setFestivalBlessingList(new ArrayList<GiftCard>());
        giftCardList1.setExpressList(new ArrayList<GiftCard>());
        Map map = new HashMap();
        for(GiftCard giftCard:giftCardList){
            if(giftCard.getType().equals("表达心意")){

                giftCardList1.getExpressList().add(giftCard);
                map.put(giftCard.getType(),giftCardList1.getExpressList());
            }
            if(giftCard.getType().equals("节日祝福")){

                giftCardList1.getFestivalBlessingList().add(giftCard);
            }
        }
        if(giftCardList==null){
            status = APIStatus.GIFT_CARD_NOT_FOUND;
        }
        return APIUtil.getResponse(status,map);
    }
}
