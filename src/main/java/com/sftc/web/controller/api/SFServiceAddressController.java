package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.SFServiceAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("sf/service/address")
public class SFServiceAddressController {

    @Resource
    private SFServiceAddressService sfServiceAddressService;

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateSFServiceAddress() throws Exception {
        return sfServiceAddressService.updateServiceAddress();
    }
}
