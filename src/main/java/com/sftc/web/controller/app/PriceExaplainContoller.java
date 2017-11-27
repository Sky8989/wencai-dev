package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.PriceExaplainVO;
import com.sftc.web.service.PriceExaplainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "顺丰同城价格说明")
@RestController
@RequestMapping(value = "price")
public class PriceExaplainContoller {
    @Autowired
    private PriceExaplainService priceExaplainService;

    @ApiOperation(value = "根据城市获取价格说明", httpMethod = "POST")
    @PostMapping(value = "get")
    public APIResponse getPriceExplainByCity(@RequestBody PriceExaplainVO priceExaplainVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(priceExaplainVO);
        return priceExaplainService.getPriceExplainByCity(request);
    }
}
