package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.SFServiceAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("sf/service/")
public class SFServiceAddressController {

    @Resource
    private SFServiceAddressService sfServiceAddressService;

    @RequestMapping(value = "address/update", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateSFServiceAddress(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.updateServiceAddress(new APIRequest(request));
    }

    @RequestMapping(value = "dynamic/rate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectRate(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.selectDynamicPrice(new APIRequest(request));
    }
}
