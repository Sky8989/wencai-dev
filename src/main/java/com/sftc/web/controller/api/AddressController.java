package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 地址接口
 * @date 2017/5/22
 * @Time 下午5:51
 */
@Controller
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse addAddress(@RequestBody Address address) throws Exception {
        return addressService.addAddress(address);
    }

    @RequestMapping(value = "/consignee", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse consigneeAddress(HttpServletRequest request) throws Exception {
        return addressService.consigneeAddress(new APIRequest(request));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse editAddress(@RequestBody Address address) throws Exception {
        return addressService.editAddress(address);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse deleteAddress(HttpServletRequest request) throws Exception {
        return addressService.deleteAddress(new APIRequest(request));
    }
}
