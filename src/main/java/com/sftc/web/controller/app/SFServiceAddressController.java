package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.SwaggerOrderVO.SFServiceRequestVO;
import com.sftc.web.service.SFServiceAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "时效计价")
@RequestMapping("sf/service")
public class SFServiceAddressController {

    @Resource
    private SFServiceAddressService sfServiceAddressService;

    @ApiOperation(value = "更新顺丰速运服务地址",httpMethod = "PUT")
    @RequestMapping(value = "/address", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updateSFServiceAddress(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.updateServiceAddress(new APIRequest(request));
    }

    @ApiOperation(value = "顺丰运费时效查询",httpMethod = "POST")
    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse selectRate(@RequestBody SFServiceRequestVO sfServiceRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(sfServiceRequestVO);
        return sfServiceAddressService.selectDynamicPrice(request);
    }

    @ApiOperation(value = "订单运费时效查询",httpMethod = "GET")
    @RequestMapping(value = "/order/rate", method = RequestMethod.GET)
    @ApiImplicitParam(name = "order_id",value = "订单id",required = true,paramType = "query",defaultValue = "C150943940840398")
    public @ResponseBody
    APIResponse selectOrderRate(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.selectOrderDynamicPrice(new APIRequest(request));
    }
}
