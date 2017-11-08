package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.PriceExaplainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "价格说明")
@RestController
@RequestMapping(value = "price")
public class PriceExaplainContoller {
    @Autowired
    private PriceExaplainService priceExaplainService;
    @ApiOperation(value = "根据城市获取价格说明", httpMethod = "POST")
    @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "query", defaultValue = "北京")
    @PostMapping(value = "get")
    public APIResponse getPriceExplainByCity(@RequestParam(value = "city") String city) {
        return priceExaplainService.getPriceExplainByCity(city);
    }
}
