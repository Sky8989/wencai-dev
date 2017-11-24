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
import com.sftc.web.model.vo.swaggerRequestVO.giftCard.DeleteGiftCardVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(description = "CMS礼品卡相关接口")
@RequestMapping("giftCard")
public class GiftCardController extends BaseController {

    @ApiOperation(value = "CMS获取礼品卡列表",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getGiftCardList(HttpServletRequest request) throws Exception {
        return giftCardService.getGiftCardList(new APIRequest(request));
    }
    @ApiOperation(value = "CMS保存礼品卡 id为0新增，id不为0修改",httpMethod = "POST")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public @ResponseBody
    APIResponse save(@RequestBody GiftCard giftCard) throws Exception {
    	APIRequest apiRequest = new APIRequest();
		apiRequest.setRequestParam(giftCard);
    			return giftCardService.save(apiRequest);	
    }
    @ApiOperation(value = "CMS删除礼品卡",httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteGiftCard(@RequestBody  DeleteGiftCardVO giftCard) throws Exception {
    	APIRequest apiRequest = new APIRequest();
		apiRequest.setRequestParam(giftCard);
    	return giftCardService.deleteGiftCard(apiRequest);
    }
    
    
}
