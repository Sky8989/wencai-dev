package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("cms/cancleedOrder")
@RestController
public class CMSCanceledOrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse selectCanceledOrderList(HttpServletRequest httpServletRequest) {
        return orderService.selectCanceledOrderList(new APIRequest(httpServletRequest));
    }


}
