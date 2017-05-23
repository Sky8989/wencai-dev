package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.sfmodel.Requests;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 地址操作接口
 * @date 17/4/1
 * @Time 下午9:30
 */
public interface AddressService {
    /**
     * 新增地址
     * @param @request
     * @return
     */
    APIResponse addAddress(Object object);
}
