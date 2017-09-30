package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.Address;
import com.sftc.web.service.AddressHistoryService;
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

    @Resource
    private AddressHistoryService addressHistoryService;

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

    @RequestMapping(value = "/geocoder", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse geocoderAddress(HttpServletRequest request) throws Exception {
        return addressService.geocoderAddress(new APIRequest(request));
    }

    @RequestMapping(value = "/distance", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse distanceAddress(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return addressService.getAddressDistance(request);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectAddressHistory(HttpServletRequest request) throws Exception {
        return addressHistoryService.selectAddressHistory(new APIRequest(request));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse deleteAddress(HttpServletRequest request) throws Exception {
        return addressHistoryService.deleteAddressHistory(new APIRequest(request));
    }
}
