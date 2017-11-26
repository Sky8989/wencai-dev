package com.sftc.web.controller.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.vo.swaggerRequest.DeleteGiftCardVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(description = "礼品卡相关接口")
@RequestMapping("giftCard")
public class GiftCardController extends BaseController {

    @ApiOperation(value = "获取所有礼品卡",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getGiftCardList(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCardList(new APIRequest(request));
    }
}
