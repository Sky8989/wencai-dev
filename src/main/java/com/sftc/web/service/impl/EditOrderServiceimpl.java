package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.EditOrder;
import com.sftc.web.service.EditOrderService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description:
 * @date 2017/4/12
 * @Time 下午3:37
 */
public class EditOrderServiceimpl extends AbstractBasicService implements EditOrderService {

    @Override
    public APIResponse insertOrder(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String postalCode = request.getParameter("postalCode");
        String provinceName = request.getParameter("provinceName");
        String cityName = request.getParameter("cityName");
        String countyName = request.getParameter("countyName");
        String detailInfo = request.getParameter("detailInfo");
        String nationalCode = request.getParameter("nationalCode");
        String telNumber = request.getParameter("telNumber");
        EditOrder editOrder = new EditOrder(userName, postalCode, provinceName,
                cityName, countyName, detailInfo, nationalCode, telNumber);
        try {
            editOrderMapper.editOrder(editOrder);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    @Override
    public APIResponse selectOrder(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        EditOrder editOrder = editOrderMapper.selectOrderById(id);
        if (editOrder == null) {
            status = APIStatus.FAIL;
        }
        return APIUtil.getResponse(status, editOrder);
    }
}
