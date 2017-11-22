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
import com.sftc.web.service.PriceExaplainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "价格说明")
@RestController
@RequestMapping(value = "price")
public class PriceExaplainContoller {
    @Autowired
    private PriceExaplainService priceExaplainService;
    @ApiOperation(value = "根据城市获取价格说明", httpMethod = "POST")
    @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "query", defaultValue = "北京")
    @PostMapping(value = "get")
    public APIResponse getPriceExplainByCity(@RequestBody PriceExaplainVO priceExaplainVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(priceExaplainVO);
        return priceExaplainService.getPriceExplainByCity(apiRequest);
    }
    
    @ApiOperation(value = "新增(修改)价格说明", httpMethod = "POST")
    @RequestMapping(value="addOrUpdate",method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addCommonQuestion(@RequestBody PriceExplain priceExplain) throws Exception {
         if(priceExplain != null && priceExplain.getId() != 0){
        	 return priceExaplainService.updatePriceExplain(priceExplain);
         }else{
        	 return priceExaplainService.addPriceExplain(priceExplain);
         }
    	
    }
    @ApiOperation(value = "删除价格说明", httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteCommonQuestion(@RequestBody int id) throws Exception {
    		return priceExaplainService.deletePriceExplain(id);
    	}
}
