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
import java.util.*;

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
        List<GiftCard> giftCardList= giftCardMapper.giftCardList();
        Map map = new HashMap();
        try {
            for (GiftCard giftCard : giftCardList) {
                List<GiftCard> list = new ArrayList();
                list.add(giftCard);
                if(map.get(giftCard.getType())!=null){
                if (map.containsKey((giftCard.getType()))) {
                    List<GiftCard> list1 = new ArrayList();
                   List<GiftCard> list2 = (List)map.get(giftCard.getType());
                    for(GiftCard giftCard1:list2) {
                        list1.add(giftCard1);
                    }
                    list1.add(list.get(0));
                    map.put(giftCard.getType(), list1);
                }else {
                    map.put(giftCard.getType(), list);
                }
                }else {

                    map.put(giftCard.getType(), list);
                }
                System.out.println(map.toString());

            }
            if (giftCardList == null) {
                status = APIStatus.GIFT_CARD_NOT_FOUND;
            }
        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }
        return APIUtil.getResponse(status,map);
    }
}
