package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerOrderVO.MultiplePackageVO;
import com.sftc.web.service.MultiplePackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.ws.rs.HttpMethod;

/**
 * 好友多包裹
 *
 * @author ： CatalpaFlat
 * @date ：Create in 14:06 2017/11/17
 */
@Api(description = "好友多包裹")
@RestController
@RequestMapping(value = "multiple")
public class MultiplePackageController {

    @Resource
    private MultiplePackageService multiplePackageService;

    @ApiOperation(value = "批量计价", httpMethod = HttpMethod.POST)
    @PostMapping(value = "valuation")
    public APIResponse batchValuation(@RequestBody @Validated MultiplePackageVO multiplePackageVO) {
        APIRequest request = new APIRequest();
        request.setRequestParam(multiplePackageVO);
        return multiplePackageService.batchValuation(request);
    }


    @ApiOperation(value = "批量下单", httpMethod = HttpMethod.POST)
    @PostMapping(value = "placeOrder")
    public APIResponse batchPlaceOrder(@RequestBody @Validated MultiplePackageVO multiplePackageVO) {
        APIRequest request = new APIRequest();
        request.setRequestParam(multiplePackageVO);
        return multiplePackageService.batchPlaceOrder(request);
    }
}
