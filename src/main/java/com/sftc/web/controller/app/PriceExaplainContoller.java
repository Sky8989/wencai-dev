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
import com.sftc.web.model.vo.swaggerRequest.DeletePriceExplain;
import com.sftc.web.service.PriceExaplainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "顺丰同城价格说明")
@RestController
@RequestMapping(value = "price")
public class PriceExaplainContoller {
    @Autowired
    private PriceExaplainService priceExaplainService;

    @ApiOperation(value = "根据城市查询价格说明", httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getPriceExplainByCity(@RequestBody PriceExaplainVO priceExaplainVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(priceExaplainVO);
        return priceExaplainService.getPriceExplainByCity(request);
    }
}
