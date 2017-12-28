package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackagePayVO;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackageVO;
import com.sftc.web.service.MultiplePackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
public class MultiplePackageController extends BaseController {

    @Resource
    private MultiplePackageService multiplePackageService;

    @ApiOperation(value = "批量计价", httpMethod = HttpMethod.POST)
    @PostMapping(value = "valuation")
    public ApiResponse batchValuation(@RequestBody @Valid MultiplePackageVO body, BindingResult result) {
        ApiResponse errorMap = super.validRequestParams(result);
        if (errorMap != null) {
            return errorMap;
        }
        return multiplePackageService.batchValuation(body);
    }

    @ApiOperation(value = "批量下单", httpMethod = HttpMethod.POST)
    @PostMapping(value = "placeOrder")
    public ApiResponse batchPlaceOrder(@RequestBody @Valid MultiplePackageVO body, BindingResult result) {
        ApiResponse errorMap = super.validRequestParams(result);
        if (errorMap != null) {
            return errorMap;
        }
        return multiplePackageService.batchPlaceOrder(body);
    }

    @ApiOperation(value = "批量支付", httpMethod = HttpMethod.POST)
    @PostMapping(value = "pay")
    public ApiResponse batchPay(@RequestBody @Valid MultiplePackagePayVO body, BindingResult result) {
        ApiResponse errorMap = super.validRequestParams(result);
        if (errorMap != null) {
            return errorMap;
        }
        return multiplePackageService.batchPay(body);
    }

    @ApiOperation(value = "是否支付成功", httpMethod = HttpMethod.POST)
    @PostMapping(value = "isPay")
    public ApiResponse isPay(@RequestBody @Valid MultiplePackagePayVO body, BindingResult result) {
        ApiResponse errorMap = super.validRequestParams(result);
        if (errorMap != null) {
            return errorMap;
        }
        return multiplePackageService.isPay(body);
    }


}
