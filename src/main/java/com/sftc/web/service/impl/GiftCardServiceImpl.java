package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.GiftCardMapper;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.dto.GiftCardList;
import com.sftc.web.service.GiftCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class GiftCardServiceImpl implements GiftCardService {
    @Resource
    private GiftCardMapper giftCardMapper;

    public APIResponse getGiftCardList(APIRequest request) {

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

        return APIUtil.getResponse(SUCCESS, giftCardLists);
    }

    /**
     * CMS 系统 获取礼品卡列表 条件查询+分页
     */
    public APIResponse selectList(APIRequest apiRequest) throws Exception {

        // 此处封装了 User的构造方法
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        GiftCard giftCard = new GiftCard(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<GiftCard> giftCardList = giftCardMapper.selectByPage(giftCard);
        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> giftCardMapper.selectByPage(giftCard));
        //  处理结果
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }

    /**
     * CMS 系统 添加礼品卡信息
     */
    public APIResponse addGiftCard(GiftCard giftCard) throws Exception {
        giftCard.setCreate_time(Long.toString(System.currentTimeMillis()));
        giftCardMapper.insertGiftCard(giftCard);
        return APIUtil.getResponse(SUCCESS, giftCard);
    }

    /**
     * CMS 系统 修改礼品卡信息
     */
    public APIResponse updateGiftCard(GiftCard giftCard) throws Exception {
        giftCardMapper.updateGiftCard(giftCard);
        return APIUtil.getResponse(SUCCESS, giftCard);
    }

    /**
     * CMS 系统 删除礼品卡信息
     */
    public APIResponse deleteGiftCard(int id) throws Exception {
        giftCardMapper.deleteGiftCard(id);
        return APIUtil.getResponse(SUCCESS, id);
    }
}
