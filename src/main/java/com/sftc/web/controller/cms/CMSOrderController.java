package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("cms/order")
public class CMSOrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse OrderList(HttpServletRequest request) throws Exception{
        // 分页查询
        //TODO 需要封装订单的快递 寄件人收件人 等复杂信息 未完成
        return orderService.selectOrderListByPage(new APIRequest(request));
    }
}
