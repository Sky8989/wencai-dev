package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.OrderExpressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/7/5.
 */
@Controller
@RequestMapping("cms/orderExpress")
public class CMSOrderExpressController {
    @Resource
    private OrderExpressService orderExpressService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse UserList(HttpServletRequest request) throws Exception{
        //cms 获取快递信息列表 分页+条件
        return orderExpressService.selectOrderExpressListByPage(new APIRequest(request));
    }
}
