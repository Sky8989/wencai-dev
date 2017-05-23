package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/5/22.
 */
@Controller
@RequestMapping("address")
public class AddressConrtoller extends AbstractBasicController{
    /**
     * 新增地址
     * @param @orderParam
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addAddress", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse friendPlaceOrder(@RequestBody Object object) throws Exception {

        return addressService.addAddress(object);
    }
}
