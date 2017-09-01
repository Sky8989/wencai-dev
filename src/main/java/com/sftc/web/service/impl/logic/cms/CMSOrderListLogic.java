package com.sftc.web.service.impl.logic.cms;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderCancelMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderCancel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Component
public class CMSOrderListLogic {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderCancelMapper orderCancelMapper;

    public APIResponse selectOrderListByPage(APIRequest request) {

        HttpServletRequest httpServletRequest = request.getRequest();
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        Order order = new Order(httpServletRequest);
//        List<Order> orderList = orderMapper.selectOrderByPage(order);
        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> orderMapper.selectOrderByPage(order));

        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }

    public APIResponse selectCanceledOrderList(APIRequest request) {

        HttpServletRequest httpServletRequest = request.getRequest();
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        OrderCancel orderCancel = new OrderCancel(httpServletRequest);
//        List<Order> orderList = orderMapper.selectOrderByPage(order);
        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> orderCancelMapper.selectCanceledOrderList(orderCancel));
        
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }
}
