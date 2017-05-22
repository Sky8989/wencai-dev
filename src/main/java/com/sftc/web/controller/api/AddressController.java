package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
