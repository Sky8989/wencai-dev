package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "礼品卡")
@RequestMapping("giftCard")
public class GiftCardController extends BaseController {

    /**
     * 获取礼卡列表
     */
    @ApiOperation(value = "获取礼品卡列表",httpMethod = "GET")
    @RequestMapping(value = "/getGiftCardList", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getGiftCardList(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCardList(new APIRequest(request));
    }
}
