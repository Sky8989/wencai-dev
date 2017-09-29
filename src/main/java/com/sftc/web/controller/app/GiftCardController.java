package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("giftCard")
public class GiftCardController extends BaseController {

    /**
     * 获取礼卡列表
     */
    @RequestMapping(value = "/getGiftCardList", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getGiftCardList(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCardList(new APIRequest(request));
    }
}
