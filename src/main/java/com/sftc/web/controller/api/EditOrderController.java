package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.EditOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description:
 * @date 2017/4/12
 * @Time 下午3:42
 */
@Controller
@RequestMapping("edit")
public class EditOrderController {

    @Resource
    private EditOrderService editOrderService;

    @RequestMapping(value = "/order", method = {RequestMethod.GET, RequestMethod.POST}, headers = "api-version=1")
    public @ResponseBody
    APIResponse editOrder(HttpServletRequest request) {
        return editOrderService.insertOrder(request);
    }
}
