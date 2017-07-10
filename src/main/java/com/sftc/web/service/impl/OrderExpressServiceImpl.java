package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.service.OrderExpressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class OrderExpressServiceImpl implements OrderExpressService {
    @Resource
    private  OrderExpressMapper orderExpressMapper;

    //cms 获取快递信息列表 分页+条件
    public APIResponse selectOrderExpressListByPage(APIRequest apiRequest) {
        APIStatus status = APIStatus.SUCCESS;
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        // 此处封装了 OrderExpress的构造方法
        OrderExpress orderExpress = new OrderExpress(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<OrderExpress> orderExpressList = orderExpressMapper.selectOrderExpressByPage(orderExpress);
        if (orderExpressList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(status, orderExpressList);
        }
    }
}
