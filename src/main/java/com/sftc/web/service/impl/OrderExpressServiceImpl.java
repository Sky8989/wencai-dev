package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.OrderExpressTransform;
import com.sftc.web.service.OrderExpressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class OrderExpressServiceImpl implements OrderExpressService {
    @Resource
    private OrderExpressMapper orderExpressMapper;

    //cms 获取快递信息列表 分页+条件
    public APIResponse selectOrderExpressListByPage(APIRequest apiRequest) {

        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        // 此处封装了 OrderExpress的构造方法
        OrderExpress orderExpress = new OrderExpress(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
//        List<OrderExpress> orderExpressList = orderExpressMapper.selectOrderExpressByPage(orderExpress);

        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> orderExpressMapper.selectOrderExpressByPage(orderExpress));
        //  处理结果
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }

    public APIResponse selectOrderExpressTransformList(APIRequest apiRequest) {

        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        // 此处封装了 OrderExpress的构造方法
        OrderExpressTransform orderExpressTransform = new OrderExpressTransform(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);

        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> orderExpressMapper.selectOrderExpressTransformList(orderExpressTransform));
        //  处理结果
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }


}
