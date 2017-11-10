package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.SwaggerRequestVO.PriceExaplainVO;
import com.sftc.web.service.PriceExaplainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
