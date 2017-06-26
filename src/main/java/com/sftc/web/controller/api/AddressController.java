package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addAddress(@RequestBody Address address) throws Exception {
        return addressService.addAddress(address);
    }

    @RequestMapping(value = "/consignee", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse consigneeAddress(HttpServletRequest request) throws Exception {
        return addressService.consigneeAddress(new APIRequest(request));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse editAddress(@RequestBody Address address) throws Exception {
        return addressService.editAddress(address);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse deleteAddress(HttpServletRequest request) throws Exception {
        return addressService.deleteAddress(new APIRequest(request));
    }

    @RequestMapping(value = "/geocoder", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse geocoderAddress(HttpServletRequest request) throws Exception {
        return addressService.geocoderAddress(new APIRequest(request));
    }
}
