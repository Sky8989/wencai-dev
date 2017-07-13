package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.GiftCard;
import com.sftc.web.service.GiftCardService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/7/11.
 * 在CMS中对礼品卡进行操作的controller
 */
@Controller
@RequestMapping("cms/giftcard")
public class CMSGiftCardController {
    @Resource
    GiftCardService giftCardService;

    /**
     * 获取所有礼物卡信息列表  分页+条件查询
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse giftCardList(HttpServletRequest httpServletRequest) throws Exception {
        return giftCardService.selectList(new APIRequest(httpServletRequest));
    }

    /**
     * 添加礼品卡信息
     *
     * @param giftCard
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addGiftCard(@RequestBody GiftCard giftCard) throws Exception {
        return giftCardService.addGiftCard(giftCard);
    }

    /**
     * 修改礼品卡信息
     *
     * @param giftCard
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateGiftCard(@RequestBody GiftCard giftCard) throws Exception {
        return giftCardService.updateGiftCard(giftCard);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse deleteGiftCard(@RequestBody Object object) throws Exception {
        int id = JSONObject.fromObject(object).containsKey("id")?JSONObject.fromObject(object).getInt("id"):0;
        return giftCardService.deleteGiftCard(id);
    }


}
