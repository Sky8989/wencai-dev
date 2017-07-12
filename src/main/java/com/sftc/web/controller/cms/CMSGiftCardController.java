package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.GiftCardService;
import org.springframework.stereotype.Controller;
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
    APIResponse evaluateList(HttpServletRequest httpServletRequest) throws Exception{
        return giftCardService.selectList(new APIRequest(httpServletRequest));
    }
}
