package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description:
 * @date 2017/4/12
 * @Time 下午3:36
 */
public interface EditOrderService {

    APIResponse insertOrder(HttpServletRequest request);
}
