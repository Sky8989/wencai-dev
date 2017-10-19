package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.SFServiceAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "时效计价")
@RequestMapping("sf/service/")
public class    SFServiceAddressController {

    @Resource
    private SFServiceAddressService sfServiceAddressService;

    @ApiOperation(value = "设置大网预约定时器",httpMethod = "POST")
    @RequestMapping(value = "address/update", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateSFServiceAddress(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.updateServiceAddress(new APIRequest(request));
    }

    @RequestMapping(value = "dynamic/rate", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse selectRate(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return sfServiceAddressService.selectDynamicPrice(request);
    }

    @RequestMapping(value = "dynamic/order/rate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectOrderRate(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.selectOrderDynamicPrice(new APIRequest(request));
    }
}