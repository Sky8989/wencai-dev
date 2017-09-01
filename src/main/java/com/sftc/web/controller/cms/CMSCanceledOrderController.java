package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.OrderCancel;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.service.OrderService;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("cms/cancleedOrder")
@RestController
public class CMSCanceledOrderController extends AbstractBasicController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse selectCanceledOrderList(HttpServletRequest httpServletRequest) {
        return orderService.selectCanceledOrderList(new APIRequest(httpServletRequest));
    }


}
