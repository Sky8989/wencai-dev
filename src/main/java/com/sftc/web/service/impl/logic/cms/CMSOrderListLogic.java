package com.sftc.web.service.impl.logic.cms;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Component
public class CMSOrderListLogic {

    @Resource
    private OrderMapper orderMapper;

    public APIResponse selectOrderListByPage(APIRequest request) {

        HttpServletRequest httpServletRequest = request.getRequest();
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        Order order = new Order(httpServletRequest);
        List<Order> orderList = orderMapper.selectOrderByPage(order);
        if (orderList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, orderList);
        }
    }
}
