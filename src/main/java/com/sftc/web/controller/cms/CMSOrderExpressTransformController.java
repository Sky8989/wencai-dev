package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.OrderExpressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("cms/orderExpressTransform")
@RestController
public class CMSOrderExpressTransformController extends AbstractBasicController {

    @Resource
    private OrderExpressService orderExpressService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse selectCanceledOrderList(HttpServletRequest httpServletRequest) {
        return orderExpressService.selectOrderExpressTransformList(new APIRequest(httpServletRequest));
    }


}
