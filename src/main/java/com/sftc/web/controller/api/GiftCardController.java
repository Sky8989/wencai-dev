package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.impl.AbstractBasicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/5/14.
 */
@Controller
@RequestMapping("giftCard")
public class GiftCardController extends AbstractBasicController{
    /**
     * 获取礼卡
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGiftCard", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCard(new APIRequest(request));
    }
    /**
     * 获取礼卡列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGiftCardList", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse getGiftCardList(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCardList(new APIRequest(request));
    }
}
