package com.sftc.web.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.PriceExplain;
import com.sftc.web.model.vo.swaggerRequest.PriceExaplainVO;
import com.sftc.web.model.vo.swaggerRequestVO.priceExaplain.DeletePriceExplain;
import com.sftc.web.service.PriceExaplainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "CMS价格说明")
@RestController
@RequestMapping(value = "price")
public class PriceExaplainContoller {
    @Autowired
    private PriceExaplainService priceExaplainService;
    @ApiOperation(value = "CMS根据城市获取价格说明", httpMethod = "POST")
    @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "query", defaultValue = "北京")
    @PostMapping(value = "get")
    public APIResponse getPriceExplainByCity(@RequestBody PriceExaplainVO priceExaplainVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(priceExaplainVO);
        return priceExaplainService.getPriceExplainByCity(apiRequest);
    }
    
    @ApiOperation(value = "CMS保存价格说明 id为0新增，id不为0修改", httpMethod = "POST")
    @RequestMapping(value="save",method = RequestMethod.POST)
    public @ResponseBody
    APIResponse save(@RequestBody PriceExplain priceExplain) throws Exception {
    	APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(priceExplain);
         return priceExaplainService.save(apiRequest);	
        		 
    	
    }
    @ApiOperation(value = "CMS删除价格说明", httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deletePriceExplain(@RequestBody DeletePriceExplain priceExplain) throws Exception {
    	APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(priceExplain);
    		return priceExaplainService.deletePriceExplain(apiRequest);
    	}
}
