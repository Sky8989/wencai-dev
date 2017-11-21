package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackagePayVO;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackageVO;
import com.sftc.web.service.MultiplePackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.ws.rs.HttpMethod;
import java.util.ArrayList;
import java.util.List;

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
    public APIResponse batchValuation(@RequestBody @Valid MultiplePackageVO multiplePackageVO, BindingResult result) {
        APIResponse errorMap = validRequestParms(result);
        if (errorMap != null) {
            return errorMap;
        }
        APIRequest request = new APIRequest();
        request.setRequestParam(multiplePackageVO);
        return multiplePackageService.batchValuation(request);
    }

    @ApiOperation(value = "批量下单", httpMethod = HttpMethod.POST)
    @PostMapping(value = "placeOrder")
    public APIResponse batchPlaceOrder(@RequestBody @Valid MultiplePackageVO multiplePackageVO, BindingResult result) {
        APIResponse errorMap = validRequestParms(result);
        if (errorMap != null) {
            return errorMap;
        }
        APIRequest request = new APIRequest();
        request.setRequestParam(multiplePackageVO);
        return multiplePackageService.batchPlaceOrder(request);
    }

    @ApiOperation(value = "批量支付", httpMethod = HttpMethod.POST)
    @PostMapping(value = "pay")
    public APIResponse batchPay(@RequestBody @Valid MultiplePackagePayVO multiplePackageVO, BindingResult result) {
        APIResponse errorMap = validRequestParms(result);
        if (errorMap != null) {
            return errorMap;
        }
        APIRequest request = new APIRequest();
        request.setRequestParam(multiplePackageVO);
        return multiplePackageService.batchPay(request);
    }

    private APIResponse validRequestParms(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            List<String> lists = new ArrayList<>();
            for (ObjectError objectError : allErrors) {
                lists.add(objectError.getDefaultMessage());
            }
            return APIUtil.submitErrorResponse("请求体不完整", lists);
        }
        return null;
    }

}
